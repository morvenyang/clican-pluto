//
//  JSEngine.h
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import <Foundation/Foundation.h>
#import <JavaScriptCore/JavaScriptCore.h>
#import "MBProgressHUD.h"

@interface JSEngine : NSObject<TTURLRequestDelegate,MBProgressHUDDelegate>{
    JSGlobalContextRef _JSContext;
    MBProgressHUD* _progressHUD;
    dispatch_queue_t queue;
}
@property (nonatomic, retain) MBProgressHUD    *progressHUD;

- (void) reloadJS;
- (NSString *)runJS:(NSString *)aJSString view:(UIView*) view;


@end
