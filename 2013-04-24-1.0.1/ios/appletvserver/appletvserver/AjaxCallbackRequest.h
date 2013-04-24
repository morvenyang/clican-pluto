//
//  AjaxCallbackRequest.h
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import <Three20Network/Three20Network.h>
#import <JavaScriptCore/JavaScriptCore.h>

@interface AjaxCallbackRequest : TTURLRequest<TTURLRequestDelegate>{
    JSObjectRef _callback;
    JSContextRef _ctx;
}

@property (nonatomic, assign) JSObjectRef callback;
@property (nonatomic, assign) JSContextRef ctx;

+ (AjaxCallbackRequest*)requestWithURL:(NSString*)URL delegate:(id /*<TTURLRequestDelegate>*/)delegate callback:(JSObjectRef) callback ctx:(JSContextRef) ctx;
@end
