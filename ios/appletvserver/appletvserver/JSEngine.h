//
//  JSEngine.h
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import <Foundation/Foundation.h>
#import <JavaScriptCore/JavaScriptCore.h>


@interface JSEngine : NSObject<TTURLRequestDelegate>{
    JSGlobalContextRef _JSContext;

}


- (void) reloadJS;
- (NSString *)runJS:(NSString *)aJSString;
- (void)search:(NSString *)keyword function:(JSObjectRef) callback;

@end
