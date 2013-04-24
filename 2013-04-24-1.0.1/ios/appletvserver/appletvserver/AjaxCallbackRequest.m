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
@end
