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
@implementation JSEngine




/**
 Lazily initializes and returns a JS context
 */


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
    
    AjaxCallbackRequest* request = [AjaxCallbackRequest
                             requestWithURL: url
                             delegate: [AppDele jsEngine] callback:callback ctx:ctx];
    
    request.cachePolicy = TTURLRequestCachePolicyMemory;
    
    TTURLDataResponse* response = [[TTURLDataResponse alloc] init];
    request.response = response;
    [request send];
    
   
    return JSValueMakeNull(ctx);
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
    
    request.cachePolicy = TTURLRequestCachePolicyMemory;
    
    TTURLDataResponse* response = [[TTURLDataResponse alloc] init];
    request.response = response;
    if(content!=NULL&&content.length>0){
        request.httpBody = [content dataUsingEncoding:NSUTF8StringEncoding];
    }
    [request send];
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

JSValueRef loadXML(JSContextRef ctx,
                    JSObjectRef function,
                    JSObjectRef thisObject,
                    size_t argumentCount,
                    const JSValueRef arguments[],
                    JSValueRef* exception){
    JSValueRef excp = NULL;
    NSString *xml = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[0], &excp));
    XmlViewController* controler = [[XmlViewController alloc] initWithXml:xml];
    [[TTNavigator navigator].topViewController.navigationController pushViewController:controler animated:YES];
    return JSValueMakeNull(ctx);
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
    JSStringRelease(str4);
    
    NSString* jsDirectory = [[AppDele localWebPathPrefix] stringByAppendingString:@"/appletv/javascript"];
    NSArray* jsArray=[[NSFileManager defaultManager] contentsOfDirectoryAtPath:jsDirectory error:nil];
    for(int i=0;i<[jsArray count];i++){
        NSString* jsPath = [[AppDele localWebPathPrefix] stringByAppendingFormat:@"/appletv/javascript/%@",[jsArray objectAtIndex:i]];
        NSString* jsContent = [NSString stringWithContentsOfFile:jsPath encoding:NSUTF8StringEncoding error:nil];
        if([jsPath rangeOfString:@"clican.js"].location!=NSNotFound){
            jsContent = [jsContent stringByReplacingOccurrencesOfString:@"simulate : 'atv'" withString:@"simulate : 'native'"];
        }
        [self runJS:jsContent];
    }
    [self runJS:@"appletv.logToServer('test');"];
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

/**
 Loads a JS library file from the app's bundle (without the .js extension)
 */
- (void)loadJSLibrary:(NSString*)libraryName
{
    NSString *library = [NSString stringWithContentsOfFile:[[NSBundle mainBundle] pathForResource:libraryName ofType:@"js"]  encoding:NSUTF8StringEncoding error:nil];
    
    NSLog(@"[JSC] loading library %@...", libraryName);
    [self runJS:library];
}

@end
