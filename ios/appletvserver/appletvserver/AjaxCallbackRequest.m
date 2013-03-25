//
//  AjaxCallbackRequest.m
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import "AjaxCallbackRequest.h"


@implementation AjaxCallbackRequest

@synthesize callback = _callback;
@synthesize ctx = _ctx;

+ (AjaxCallbackRequest*)requestWithURL:(NSString*)URL delegate:(id /*<TTURLRequestDelegate>*/)delegate callback:(JSObjectRef) callback ctx:(JSContextRef) ctx; {
    AjaxCallbackRequest* req = [[[self alloc] initWithURL:URL delegate:delegate] autorelease];
    req.callback = callback;
    req.ctx = ctx;
    return req;
}

#pragma mark -
#pragma mark TTURLRequestDelegate

- (void)requestDidFinishLoad:(TTURLRequest*)request
{
    @try {
        TTURLDataResponse* response = request.response;
        
        NSString* content = [[NSString alloc] initWithData:[response data] encoding:NSUTF8StringEncoding];
        NSLog(@"content:%@" ,content);
        JSValueRef args[1];
        args[0] = JSValueMakeString(self.ctx,JSStringCreateWithUTF8CString([content UTF8String]));
        JSObjectCallAsFunction(self.ctx,self.callback,NULL,1,args,NULL);
    }
    @catch (NSException *exception) {
        TTAlert([NSString stringWithFormat:@"错误:%@",[exception name]]);
        
    }
}

- (void)request:(TTURLRequest*)request didFailLoadWithError:(NSError*)error {
    NSLog(@"request:%@ didFailLoadWithError:%@", request, error);
}
@end
