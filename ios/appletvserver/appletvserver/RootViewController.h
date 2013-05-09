//
//  RootViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-3-12.
//
//

#import <UIKit/UIKit.h>
#import "MBProgressHUD.h"
#import "ConfigViewController.h"
@interface RootViewController : UITabBarController<ScriptRefreshDelegate,MBProgressHUDDelegate>{
    MBProgressHUD* _progressHUD;
}
@property (nonatomic, retain) MBProgressHUD    *progressHUD;
@end
