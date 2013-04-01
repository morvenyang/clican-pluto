//
//  ConfigViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-3-31.
//
//

#import <Three20UI/Three20UI.h>
#import "MBProgressHUD.h"
#import "ConfigDataSource.h"
@interface ConfigViewController : TTTableViewController<UITextFieldDelegate,MBProgressHUDDelegate,UITableViewDelegate,ConfigCallback>{
    UITextField* _serverIPField;
    UIButton* _syncButton;
    UIButton* _clearCacheButton;
    MBProgressHUD* _progressHUD;
    TTTableControlItem* _clearCacheItem;
    UITextField* _atvDeviceIdField;
}
@property (nonatomic, retain) UITextField *serverIPField;
@property (nonatomic, retain) UIButton *syncButton;
@property (nonatomic, retain) UIButton *clearCacheButton;
@property (nonatomic, retain) MBProgressHUD    *progressHUD;
@property (nonatomic, retain) TTTableControlItem    *clearCacheItem;
@property (nonatomic, retain) UITextField *atvDeviceIdField;
@end
