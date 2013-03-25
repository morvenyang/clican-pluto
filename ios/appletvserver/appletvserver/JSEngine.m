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


- (void) reloadJS{
    _JSContext = JSGlobalContextCreate(NULL);
    NSString* jsDirectory = [[AppDele localWebPathPrefix] stringByAppendingString:@"/appletv/javascript"];
    NSArray* jsArray=[[NSFileManager defaultManager] contentsOfDirectoryAtPath:jsDirectory error:nil];
    for(int i=0;i<[jsArray count];i++){
        NSString* jsPath = [jsArray objectAtIndex:i];
        NSString* jsContent = [NSString stringWithContentsOfFile:jsPath encoding:NSUTF8StringEncoding error:nil];
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
