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
    UIButton* _refreshIPButton;
    UIButton* _clearCacheButton;
    MBProgressHUD* _progressHUD;
    TTTableControlItem* _clearCacheItem;
    UITextField* _atvDeviceIdField;
    TTTableStyledTextItem* _xunleiStatusItem;
    UISwitch* _networkSwitchy;
    TTTableControlItem* _refreshIPItem;
}
@property (nonatomic, retain) UITextField *serverIPField;
@property (nonatomic, retain) UIButton *syncButton;
@property (nonatomic, retain) UIButton *refreshIPButton;
@property (nonatomic, retain) UIButton *clearCacheButton;
@property (nonatomic, retain) MBProgressHUD    *progressHUD;
@property (nonatomic, retain) TTTableControlItem    *clearCacheItem;
@property (nonatomic, retain) UITextField *atvDeviceIdField;
@property (nonatomic, retain) TTTableStyledTextItem* xunleiStatusItem;
@property (nonatomic, retain) UISwitch* networkSwitchy;
@property (nonatomic, retain) TTTableControlItem* refreshIPItem;
@end


@protocol ScriptRefreshDelegate <NSObject>

- (void)refreshScript;

@end