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
@implementation JSEngine




/**
 Lazily initializes and returns a JS context
 */


JSValueRef makeProxyRequest(JSContextRef ctx,
                               JSObjectRef function,
                               JSObjectRef thisObject,
                               size_t argumentCount,
                               const JSValueRef arguments[],
                               JSValueRef* exception){
    JSValueRef excp = NULL;
    NSString *url = (__bridge_transfer NSString*)JSStringCopyCFString(kCFAllocatorDefault, (JSStringRef)JSValueToStringCopy(ctx, arguments[0], &excp));
    NSLog(@"makeRequest:%@",url);
    JSObjectRef callback = (JSObjectRef)JSValueToObject(ctx, arguments[1], &excp);
    JSValueRef args[1];
    args[0] = JSValueMakeString(ctx,JSStringCreateWithUTF8CString("content"));
    JSObjectCallAsFunction(ctx,callback,NULL,1,args,NULL);
    return JSValueMakeNull(ctx);
}

JSValueRef makeProxyPostRequest(JSContextRef ctx,
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
    JSValueRef args[1];
    args[0] = JSValueMakeString(ctx,JSStringCreateWithUTF8CString("content"));
    JSObjectCallAsFunction(ctx,callback,NULL,1,args,NULL);
    return JSValueMakeNull(ctx);
}

- (void) reloadJS{
    _JSContext = JSGlobalContextCreate(NULL);
    
    JSStringRef str1 = JSStringCreateWithUTF8CString("makeProxyRequest");
    JSObjectRef func1 = JSObjectMakeFunctionWithCallback(_JSContext, str1, makeProxyRequest);
    JSObjectSetProperty(_JSContext, JSContextGetGlobalObject(_JSContext), str1, func1, kJSPropertyAttributeNone, NULL);
    JSStringRelease(str1);
    
    JSStringRef str2 = JSStringCreateWithUTF8CString("makeProxyPostRequest");
    JSObjectRef func2 = JSObjectMakeFunctionWithCallback(_JSContext, str2, makeProxyPostRequest);
    JSObjectSetProperty(_JSContext, JSContextGetGlobalObject(_JSContext), str2, func2, kJSPropertyAttributeNone, NULL);
    JSStringRelease(str2);
    
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
