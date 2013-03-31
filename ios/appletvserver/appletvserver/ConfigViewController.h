//
//  ConfigViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-3-31.
//
//

#import <Three20UI/Three20UI.h>
#import "MBProgressHUD.h"

@interface ConfigViewController : TTTableViewController<UITextFieldDelegate,MBProgressHUDDelegate>{
    UITextField* _serverIPField;
    UIButton* _syncButton;
    MBProgressHUD* _progressHUD;
}
@property (nonatomic, retain) UITextField *serverIPField;
@property (nonatomic, retain) UIButton *syncButton;
@property (nonatomic, retain) MBProgressHUD    *progressHUD;
@end
