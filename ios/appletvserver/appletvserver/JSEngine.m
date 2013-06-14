//
//  JSEngine.m
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import "JSEngine.h"
#import <AddressBook/AddressBook.h>
#import "AppDelegate.h"
#import "AjaxCallbackRequest.h"
#import "InputViewController.h"
#import "XmlViewController.h"
#import "Constants.h"
#import "URLDataHeaderResponse.h"
#import "ASIHTTPRequest.h"
#import <extThree20JSON/SBJsonWriter.h>
#import <extThree20JSON/SBJsonParser.h>
#import "AtvUtil.h"
@implementation JSEngine

JSValueRef logToServer(JSContextRef ctx,
                           JSObjectRef function,
                           JSObjectRef thisObject,
                           size_t argumentCount,
                           const JSValueRef arguments[],
                           JSValueRef* exception){
    JSValueRef excp = NULL;
    NSString *log = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[0], &excp));
    NSLog(@"log=%@",log);
    
    return JSValueMakeNull(ctx);
}

JSValueRef readLocalFile(JSContextRef ctx,
                       JSObjectRef function,
                       JSObjectRef thisObject,
                       size_t argumentCount,
                       const JSValueRef arguments[],
                       JSValueRef* exception){
    JSValueRef excp = NULL;
    NSString *url = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[0], &excp));
    NSLog(@"http url:%@",url);
    NSString *filePath;
    if([url rangeOfString:@"http://localhost:8080"].location!=NSNotFound){
        filePath= [url stringByReplacingOccurrencesOfString:@"http://localhost:8080" withString:[AppDele localWebPathPrefix]];
    }else{
        filePath= [url stringByReplacingOccurrencesOfString:[NSString stringWithFormat:@"http://%@:8080",[AppDele ipAddress]] withString:[AppDele localWebPathPrefix]]; 
    }
    NSRange range = [filePath rangeOfString:@"?"];
    filePath = [filePath substringToIndex:range.location];
    NSLog(@"filePath:%@",filePath);
    NSString* content =[NSString stringWithContentsOfFile:filePath encoding:NSUTF8StringEncoding error:nil];
    return JSValueMakeString(ctx, JSStringCreateWithUTF8CString([content UTF8String]));
}


JSValueRef makeRequest(JSContextRef ctx,
                               JSObjectRef function,
                               JSObjectRef thisObject,
                               size_t argumentCount,
                               const JSValueRef arguments[],
                               JSValueRef* exception){
    JSValueRef excp = NULL;
    NSString *url = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[0], &excp));
    NSLog(@"makeRequest:%@",url);
    
    JSObjectRef callback = (JSObjectRef)JSValueToObject(ctx, arguments[1], &excp);
    NSString *headersstr = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[2], &excp));
    
    AjaxCallbackRequest* request = [AjaxCallbackRequest
                             requestWithURL: url
                             delegate: nil callback:callback ctx:ctx];
    if ([url rangeOfString:@"/appletv"].location!=NSNotFound) {
        request.cachePolicy = TTURLRequestCachePolicyNoCache;
    }else{
        request.cachePolicy = TTURLRequestCachePolicyDefault;
    }
    if(headersstr!=nil){
        SBJsonParser *parser = [[SBJsonParser alloc] init];
        NSDictionary* jsonHeaders = [parser objectWithString:headersstr];
        [request.headers setValuesForKeysWithDictionary:jsonHeaders];
    }
    
    
    [request.headers setValue:@"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22" forKey:@"User-Agent"];
    URLDataHeaderResponse* response = [[URLDataHeaderResponse alloc] init];
    request.response = response;
    [TTURLRequestQueue mainQueue].suspended=NO;
    [request sendSynchronously];
    [JSEngine processResponse:request];
    return JSValueMakeNull(ctx);
}

+(void) processResponse:(AjaxCallbackRequest*) request{
    @try {
        URLDataHeaderResponse* response = request.response;
        NSData* data = [response data];
        NSLog(@"data length:%i",[data length]);
        NSString* charset = [[response allHeaders] objectForKey:@"Content-Type"];
        NSStringEncoding enc = NSUTF8StringEncoding;
        if(charset!=nil){
            charset = [charset uppercaseString];
            if([charset rangeOfString:@"GBK"].location!=NSNotFound||[charset rangeOfString:@"GB2312"].location!=NSNotFound){
                enc = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
            }
        }
        NSString* content =[[NSString alloc] initWithData:data encoding:enc];
        if(content==nil&&[data length]>0){
            enc=CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
            content = [[NSString alloc] initWithData:data encoding:enc];
        }
        NSLog(@"handler response for : %@",request.urlPath);
        
        if(content==nil||[content length]==0){
            NSLog(@"response is empty");
            JSValueRef args[1];
            args[0] = JSValueMakeNull(request.ctx);
            JSObjectCallAsFunction(request.ctx,request.callback,NULL,1,args,NULL);
        }else{
            JSValueRef args[1];
            args[0] = JSValueMakeString(request.ctx,JSStringCreateWithUTF8CString([content UTF8String]));
            JSObjectCallAsFunction(request.ctx,request.callback,NULL,1,args,NULL);
        }
    }
    @catch (NSException *exception) {
        TTAlert([NSString stringWithFormat:@"错误:%@",[exception name]]);
        
    }
}
JSValueRef makePostRequest(JSContextRef ctx,
                            JSObjectRef function,
                            JSObjectRef thisObject,
                            size_t argumentCount,
                            const JSValueRef arguments[],
                            JSValueRef* exception){
    JSValueRef excp = NULL;
    NSString *url = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[0], &excp));
    NSLog(@"makePostRequest:%@",url);
    NSString *content = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[1], &excp));
    NSLog(@"makePostRequest:%@",content);
    JSObjectRef callback = (JSObjectRef)JSValueToObject(ctx, arguments[2], &excp);
    AjaxCallbackRequest* request = [AjaxCallbackRequest
                                    requestWithURL: url
                                    delegate: [AppDele jsEngine] callback:callback ctx:ctx];
    
    request.cachePolicy = TTURLRequestCachePolicyNoCache;
    request.contentType=@"application/x-www-form-urlencoded";
    request.httpMethod = @"POST";
    [request.headers setValue:@"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.172 Safari/537.22" forKey:@"User-Agent"];
    URLDataHeaderResponse* response = [[URLDataHeaderResponse alloc] init];
    if(content!=NULL&&content.length>0){
        request.httpBody = [content dataUsingEncoding:NSUTF8StringEncoding];
    }
    request.response = response;
    [request sendSynchronously];
    [JSEngine processResponse:request];
    return JSValueMakeNull(ctx);
}

JSValueRef showInpuTextPage(JSContextRef ctx,
                           JSObjectRef function,
                           JSObjectRef thisObject,
                           size_t argumentCount,
                           const JSValueRef arguments[],
                           JSValueRef* exception){
    JSValueRef excp = NULL;
    NSString *label = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[0], &excp));

    NSString *instructions = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[1], &excp));
    NSString *initialText = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[3], &excp));
    JSObjectRef callback = (JSObjectRef)JSValueToObject(ctx, arguments[2], &excp);
    InputViewController* controler = [[InputViewController alloc] initWithLabel:label instruction:instructions initialText:initialText callback:callback ctx:ctx];
    [[TTNavigator navigator].topViewController.navigationController pushViewController:controler animated:YES];
    return JSValueMakeNull(ctx);
}

JSValueRef getDeviceId(JSContextRef ctx,
                    JSObjectRef function,
                    JSObjectRef thisObject,
                    size_t argumentCount,
                    const JSValueRef arguments[],
                    JSValueRef* exception){
    NSString* deviceId = AppDele.atvDeviceId;
    NSLog(@"deviceId=%@",deviceId);
    return JSValueMakeString(ctx, JSStringCreateWithUTF8CString([deviceId UTF8String]));
}

JSValueRef getValue(JSContextRef ctx,
                    JSObjectRef function,
                    JSObjectRef thisObject,
                    size_t argumentCount,
                    const JSValueRef arguments[],
                    JSValueRef* exception){
    JSValueRef excp = NULL;
    NSString *key = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[0], &excp));

    NSString* value = [[NSUserDefaults standardUserDefaults] objectForKey:key];
    if(value==NULL||value.length==0){
        return JSValueMakeNull(ctx);
    }else{
        return JSValueMakeString(ctx, JSStringCreateWithUTF8CString([value UTF8String]));
    }
}

JSValueRef setValue(JSContextRef ctx,
                            JSObjectRef function,
                            JSObjectRef thisObject,
                            size_t argumentCount,
                            const JSValueRef arguments[],
                            JSValueRef* exception){
    JSValueRef excp = NULL;
    NSString *key = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[0], &excp));
    NSString *value = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[1], &excp));
    [[NSUserDefaults standardUserDefaults] setValue:value forKey:key];
    return JSValueMakeNull(ctx);
}

JSValueRef downloadMp4(JSContextRef ctx,
                    JSObjectRef function,
                    JSObjectRef thisObject,
                    size_t argumentCount,
                    const JSValueRef arguments[],
                    JSValueRef* exception){
    JSValueRef excp = NULL;
    NSString *mp4Url = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[0], &excp));
    [[AppDele downloadProcess] downloadMp4:mp4Url];
    return JSValueMakeNull(ctx);
}

JSValueRef downloadM3u8(JSContextRef ctx,
                       JSObjectRef function,
                       JSObjectRef thisObject,
                       size_t argumentCount,
                       const JSValueRef arguments[],
                       JSValueRef* exception){
    JSValueRef excp = NULL;
    NSString *m3u8Url = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[0], &excp));
    [[AppDele downloadProcess] downloadM3u8:m3u8Url];
    return JSValueMakeNull(ctx);
}

JSValueRef loadXML(JSContextRef ctx,
                    JSObjectRef function,
                    JSObjectRef thisObject,
                    size_t argumentCount,
                    const JSValueRef arguments[],
                    JSValueRef* exception){
    @try {
        JSValueRef excp = NULL;
        NSString *xml = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[0], &excp));
        if(xml==NULL||xml.length==0){
            xml = [JSEngine getDialog:@"加载XML错误" desc:@"无法获得相关内容"];
        }

        UIViewController* currentController = [TTNavigator navigator].topViewController;
        if([currentController isKindOfClass:[XmlViewController class]]){
            XmlViewController* xmlController =(XmlViewController*)currentController;
            [xmlController appendXml:xml];
        }else{
            XmlViewController* controler = [[XmlViewController alloc] initWithXml:xml];
            [[TTNavigator navigator].topViewController.navigationController pushViewController:controler animated:NO];
        }

        JSValueRef ref = JSValueMakeNull(ctx);
        return ref;
    }
    @catch (NSException *exception) {
        NSLog(@"Error occured when load xml %@",exception);
    }

    
}

JSValueRef loadURL(JSContextRef ctx,
                   JSObjectRef function,
                   JSObjectRef thisObject,
                   size_t argumentCount,
                   const JSValueRef arguments[],
                   JSValueRef* exception){
    JSValueRef excp = NULL;
    NSString *url = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[0], &excp));
    @try {
       
        ASIHTTPRequest *req = [ASIHTTPRequest requestWithURL:[NSURL URLWithString:url]];
        [req setShouldContinueWhenAppEntersBackground:YES];
        [req startSynchronous];
        NSError *error = [req error];
        NSString* content =nil;
        if (!error) {
            NSData* contentData = [req responseData];
            content = [[NSString alloc] initWithData:contentData encoding:NSUTF8StringEncoding];
        }else{
            content= [JSEngine getDialog:@"加载URL错误，无法获得相关内容" desc:[NSString stringWithFormat:@"URL:%@",url]];
        }

        UIViewController* currentController = [TTNavigator navigator].topViewController;
        if([currentController isKindOfClass:[XmlViewController class]]){
            XmlViewController* xmlController =(XmlViewController*)currentController;
            [xmlController appendXml:content];
        }else{
            XmlViewController* controler = [[XmlViewController alloc] initWithXml:content];
            [[TTNavigator navigator].topViewController.navigationController pushViewController:controler animated:NO];
        }
        
        JSValueRef ref = JSValueMakeNull(ctx);
        return ref;
    }
    @catch (NSException *exception) {
        NSLog(@"error occured when load url %@",exception);
    }
}

+(NSString*) getDialog:(NSString*) title desc:(NSString*) desc{
    NSString* result = [NSString stringWithFormat:@"<?xml version=\"1.0\" encoding=\"UTF-8\"><atv><body><dialog id=\"dialog\"><title><![CDATA[%@]]></title><description><![CDATA[%@]]></description></dialog></body></atv>",title,desc];
    return result;
}


- (void) reloadJS{
    _JSContext = JSGlobalContextCreate(NULL);
    
    JSStringRef str1 = JSStringCreateWithUTF8CString("native_makeRequest");
    JSObjectRef func1 = JSObjectMakeFunctionWithCallback(_JSContext, str1, makeRequest);
    JSObjectSetProperty(_JSContext, JSContextGetGlobalObject(_JSContext), str1, func1, kJSPropertyAttributeNone, NULL);
    JSStringRelease(str1);
    
    JSStringRef str2 = JSStringCreateWithUTF8CString("native_makePostRequest");
    JSObjectRef func2 = JSObjectMakeFunctionWithCallback(_JSContext, str2, makePostRequest);
    JSObjectSetProperty(_JSContext, JSContextGetGlobalObject(_JSContext), str2, func2, kJSPropertyAttributeNone, NULL);
    JSStringRelease(str2);
    
    JSStringRef str3 = JSStringCreateWithUTF8CString("native_showInputTextPage");
    JSObjectRef func3 = JSObjectMakeFunctionWithCallback(_JSContext, str3, showInpuTextPage);
    JSObjectSetProperty(_JSContext, JSContextGetGlobalObject(_JSContext), str3, func3, kJSPropertyAttributeNone, NULL);
    JSStringRelease(str3);
    
    JSStringRef str4 = JSStringCreateWithUTF8CString("native_setValue");
    JSObjectRef func4 = JSObjectMakeFunctionWithCallback(_JSContext, str4, setValue);
    JSObjectSetProperty(_JSContext, JSContextGetGlobalObject(_JSContext), str4, func4, kJSPropertyAttributeNone, NULL);
    JSStringRelease(str4);
    
    JSStringRef str5 = JSStringCreateWithUTF8CString("native_getValue");
    JSObjectRef func5 = JSObjectMakeFunctionWithCallback(_JSContext, str5,getValue);
    JSObjectSetProperty(_JSContext, JSContextGetGlobalObject(_JSContext), str5, func5, kJSPropertyAttributeNone, NULL);
    JSStringRelease(str5);
    
    JSStringRef str6 = JSStringCreateWithUTF8CString("native_loadXML");
    JSObjectRef func6 = JSObjectMakeFunctionWithCallback(_JSContext, str6,loadXML);
    JSObjectSetProperty(_JSContext, JSContextGetGlobalObject(_JSContext), str6, func6, kJSPropertyAttributeNone, NULL);
    JSStringRelease(str6);
    
    
    JSStringRef str7 = JSStringCreateWithUTF8CString("native_readLocalFile");
    JSObjectRef func7 = JSObjectMakeFunctionWithCallback(_JSContext, str7,readLocalFile);
    JSObjectSetProperty(_JSContext, JSContextGetGlobalObject(_JSContext), str7, func7, kJSPropertyAttributeNone, NULL);
    JSStringRelease(str7);
    
    JSStringRef str8 = JSStringCreateWithUTF8CString("native_loadURL");
    JSObjectRef func8 = JSObjectMakeFunctionWithCallback(_JSContext, str8,loadURL);
    JSObjectSetProperty(_JSContext, JSContextGetGlobalObject(_JSContext), str8, func8, kJSPropertyAttributeNone, NULL);
    JSStringRelease(str8);
    
    JSStringRef str9 = JSStringCreateWithUTF8CString("native_logToServer");
    JSObjectRef func9 = JSObjectMakeFunctionWithCallback(_JSContext, str9,logToServer);
    JSObjectSetProperty(_JSContext, JSContextGetGlobalObject(_JSContext), str9, func9, kJSPropertyAttributeNone, NULL);
    JSStringRelease(str9);
    
    JSStringRef str10 = JSStringCreateWithUTF8CString("native_getDeviceId");
    JSObjectRef func10 = JSObjectMakeFunctionWithCallback(_JSContext, str10,getDeviceId);
    JSObjectSetProperty(_JSContext, JSContextGetGlobalObject(_JSContext), str10, func10, kJSPropertyAttributeNone, NULL);
    JSStringRelease(str10);
    
    JSStringRef str11 = JSStringCreateWithUTF8CString("native_downloadMp4");
    JSObjectRef func11 = JSObjectMakeFunctionWithCallback(_JSContext, str11,downloadMp4);
    JSObjectSetProperty(_JSContext, JSContextGetGlobalObject(_JSContext), str11, func11, kJSPropertyAttributeNone, NULL);
    JSStringRelease(str11);
    
    JSStringRef str12 = JSStringCreateWithUTF8CString("native_downloadM3u8");
    JSObjectRef func12 = JSObjectMakeFunctionWithCallback(_JSContext, str12,downloadM3u8);
    JSObjectSetProperty(_JSContext, JSContextGetGlobalObject(_JSContext), str12, func12, kJSPropertyAttributeNone, NULL);
    JSStringRelease(str12);
    
    NSString* jsDirectory = [[AppDele localWebPathPrefix] stringByAppendingString:@"/appletv/javascript"];
    NSArray* jsArray=[[NSFileManager defaultManager] contentsOfDirectoryAtPath:jsDirectory error:nil];
    for(int i=0;i<[jsArray count];i++){
        if([[jsArray objectAtIndex:i] isEqualToString:@"load.js"]){
            continue;
        }
        NSString* jsPath = [[AppDele localWebPathPrefix] stringByAppendingFormat:@"/appletv/javascript/%@",[jsArray objectAtIndex:i]];
        NSString* jsContent = [NSString stringWithContentsOfFile:jsPath encoding:NSUTF8StringEncoding error:nil];
        
       
        NSLog(@"load %@",[jsPath lastPathComponent]);
        if([jsPath rangeOfString:@"clican.js"].location!=NSNotFound){
            NSString* ipAddress;
            if(AppDele.proxy){
                ipAddress= @"localhost:8080";
            }else{
                ipAddress= [AppDele.ipAddress stringByAppendingString:@":8080"];
            }
            
            NSRange matchRange1 = [jsContent rangeOfString:@"local.clican.org"];
            NSRange matchRange2 = [jsContent rangeOfString:@"/appletv"];
            
            
            NSString* matchString = [jsContent substringWithRange:NSMakeRange(matchRange1.location, matchRange2.location-matchRange1.location)];
            jsContent = [jsContent stringByReplacingOccurrencesOfString:matchString withString:ipAddress];
            jsContent = [jsContent stringByReplacingOccurrencesOfString:@"simulate : 'atv'" withString:@"simulate : 'native'"];
             jsContent = [jsContent stringByReplacingOccurrencesOfString:@"http://www.clican.org" withString:AppDele.serverIP];
        }
        
        [self runJS:jsContent];
    }
}
/**
 Runs a string of JS in this instance's JS context and returns the result as a string
 */
- (NSString *)runJS:(NSString *)aJSString
{
    if (!aJSString) {
        NSLog(@"[JSC] JS String is empty!");
        return nil;
    }

    JSStringRef scriptJS = JSStringCreateWithUTF8CString([aJSString UTF8String]);
    JSValueRef exception = NULL;
    
    JSValueRef result = JSEvaluateScript(_JSContext, scriptJS, NULL, NULL, 0, &exception);
    NSString *res = nil;
    
    if (!result) {
        if (exception) {
            JSStringRef exceptionArg = JSValueToStringCopy(_JSContext, exception, NULL);
            NSString* exceptionRes = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, exceptionArg);
            
            JSStringRelease(exceptionArg);
            NSLog(@"[JSC] JavaScript exception: %@", exceptionRes);
        }
        
        NSLog(@"[JSC] No result returned");
    } else {
        JSStringRef jstrArg = JSValueToStringCopy(_JSContext, result, NULL);
        res = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, jstrArg);
        
        JSStringRelease(jstrArg);
    }
    
    JSStringRelease(scriptJS);

    return res;
}
- (void)search:(NSString *)keyword function:(JSObjectRef) callback{
    NSLog(@"Submit");
    NSString* content = [AtvUtil encodeURL:keyword];
    NSLog(@"content1:%@" ,content);
    @try {
        JSValueRef args[1];
        NSLog(@"content2:%@" ,content);
        JSStringRef ref = JSStringCreateWithUTF8CString([content UTF8String]);
        NSLog(@"content3:%@" ,content);
        args[0] = JSValueMakeString(_JSContext,ref);
        NSLog(@"content4:%@" ,content);
        JSObjectCallAsFunction(_JSContext,callback,NULL,1,args,NULL);
        NSLog(@"content5:%@" ,content);
    }
    @catch (NSException *exception) {
        NSLog(@"exception occured when invoke search %@",exception.description);
    }
}
#pragma mark -
#pragma mark TTURLRequestDelegate
- (void)request:(AjaxCallbackRequest*)request didFailLoadWithError:(NSError*)error {
    NSLog(@"request:%@ didFailLoadWithError:%@", request, error);
}
@end
