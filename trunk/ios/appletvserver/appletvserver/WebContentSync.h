//
//  WebContentSync.h
//  appletvserver
//
//  Created by zhang wei on 13-3-22.
//
//

#import <Foundation/Foundation.h>
#import "MBProgressHUD.h"
#import "ASIHTTPRequest.h"

@interface WebContentSync : NSObject<ASIProgressDelegate>{
    MBProgressHUD* _progressHUD;
}
@property (nonatomic, retain) MBProgressHUD    *progressHUD;
-(void) syncWebContent:(MBProgressHUD*) progress;
@end
