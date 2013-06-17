//
//  ConfigViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-3-31.
//
//

#import "ConfigViewController.h"
#import "Constants.h"
#import "AppDelegate.h"
#import "AtvUtil.h"
@implementation ConfigViewController

@synthesize serverIPField = _serverIPField;
@synthesize syncButton = _syncButton;
@synthesize refreshIPButton = _refreshIPButton;
@synthesize progressHUD = _progressHUD;
@synthesize clearCacheButton = _clearCacheButton;
@synthesize clearCacheItem = _clearCacheItem;
@synthesize atvDeviceIdField = _atvDeviceIdField;
@synthesize xunleiStatusItem = _xunleiStatusItem;
@synthesize networkSwitchy = _networkSwitchy;
@synthesize refreshIPItem = _refreshIPItem;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"设置";
        self.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"设置" image:nil tag:4] autorelease];
        self.tableViewStyle = UITableViewStyleGrouped;
        self.serverIPField = [[[UITextField alloc] init] autorelease];
        self.serverIPField.font = [UIFont fontWithName:@"Microsoft YaHei" size:12];
        self.serverIPField.placeholder = ATV_SERVER_DEFAULT_IP;
        
        self.serverIPField.autocorrectionType = UITextAutocorrectionTypeNo;
        self.serverIPField.keyboardType = UIKeyboardTypeDefault;
        self.serverIPField.returnKeyType = UIReturnKeyDone;
        
        self.serverIPField.clearButtonMode = UITextFieldViewModeAlways;
        self.serverIPField.delegate = self;
        [self.serverIPField setAccessibilityLabel:@"服务器IP"];
        
        self.syncButton = [UIButton buttonWithType:UIButtonTypeCustom];
        UIImage* syncButtonImage = [UIImage imageNamed:@"sync.png"];
        [self.syncButton setImage:syncButtonImage forState:UIControlStateNormal];
        [self.syncButton addTarget:self action:@selector(syncAction) forControlEvents: UIControlEventTouchUpInside];
        
        self.refreshIPButton = [UIButton buttonWithType:UIButtonTypeCustom];
        UIImage* refreshIPButtonImage = [UIImage imageNamed:@"sync.png"];
        [self.refreshIPButton setImage:refreshIPButtonImage forState:UIControlStateNormal];
        [self.refreshIPButton addTarget:self action:@selector(refreshIPAction) forControlEvents: UIControlEventTouchUpInside];
        
        self.clearCacheButton = [UIButton buttonWithType:UIButtonTypeCustom];
        
        UIImage* clearCacheButtonImage = [UIImage imageNamed:@"sync.png"];
        [self.clearCacheButton setImage:clearCacheButtonImage forState:UIControlStateNormal];
        [self.clearCacheButton addTarget:self action:@selector(clearCacheAction) forControlEvents: UIControlEventTouchUpInside];
        
        
        self.atvDeviceIdField = [[[UITextField alloc] init] autorelease];
        self.atvDeviceIdField.font = [UIFont fontWithName:@"Microsoft YaHei" size:12];
        self.atvDeviceIdField.placeholder = AppDele.atvDeviceId;
        
        self.atvDeviceIdField.autocorrectionType = UITextAutocorrectionTypeNo;
        self.atvDeviceIdField.keyboardType = UIKeyboardTypeDefault;
        self.atvDeviceIdField.returnKeyType = UIReturnKeyDone;
        
        self.atvDeviceIdField.clearButtonMode = UITextFieldViewModeAlways;
        self.atvDeviceIdField.delegate = self;
        [self.atvDeviceIdField setAccessibilityLabel:@"AppleTV DeviceID"];
    }
    return self;
}

-(void) dealloc{
    TT_RELEASE_SAFELY(_serverIPField);
    TT_RELEASE_SAFELY(_syncButton);
    TT_RELEASE_SAFELY(_clearCacheButton);
    TT_RELEASE_SAFELY(_clearCacheItem);
    TT_RELEASE_SAFELY(_atvDeviceIdField);
    TT_RELEASE_SAFELY(_xunleiStatusItem);
    TT_RELEASE_SAFELY(_refreshIPButton);
    TT_RELEASE_SAFELY(_networkSwitchy);
    TT_RELEASE_SAFELY(_refreshIPItem);
    [super dealloc];
}
-(void)refreshIPAction{
    AppDele.ipAddress = [AtvUtil getIPAddress];
    self.refreshIPItem.caption = [NSString stringWithFormat:@"本机IP %@",AppDele.ipAddress];
    _flags.isModelDidLoadInvalid = YES;
    [self invalidateView];
}
-(void)syncAction{
    NSLog(@"sync script manually");
    self.progressHUD = [[[MBProgressHUD alloc] initWithView:self.view] autorelease];
    self.progressHUD.delegate = self;
    self.progressHUD.labelText = @"加载脚本中...";
    [self.view addSubview:self.progressHUD];
    [self.view bringSubviewToFront:self.progressHUD];
    [self.progressHUD show:YES];
    [NSThread detachNewThreadSelector:@selector(syncScript:) toTarget:self withObject:nil];
}

-(void)clearCacheAction{
    NSLog(@"clear cache");
    self.progressHUD = [[[MBProgressHUD alloc] initWithView:self.view] autorelease];
    self.progressHUD.delegate = self;
    self.progressHUD.labelText = @"清除缓存中...";
    [self.view addSubview:self.progressHUD];
    [self.view bringSubviewToFront:self.progressHUD];
    [self.progressHUD show:YES];
    [[TTURLCache sharedCache] removeAll:YES];
    self.clearCacheItem.caption = @"清空缓存（ 0.0 Mb）";
    
    [self.progressHUD hide:YES];
    _flags.isModelDidLoadInvalid = YES;
    [self invalidateView];
}

- (void) syncScript:(id)object{
    NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
    [[AppDele webContentSync] syncWebContent:self.progressHUD force:YES];
    [[AppDele jsEngine] reloadJS];
    for(int i=0;i<[AppDele scriptRefreshDelegateArray].count;i++){
        id<ScriptRefreshDelegate> scriptRefreshDelegate = [[AppDele scriptRefreshDelegateArray] objectAtIndex:i];
        [scriptRefreshDelegate refreshScript];
    }
    [self.progressHUD hide:YES];
    [pool release];
}

-(void)updateConfig{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"/Three20/"];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    unsigned long long folderSize = 0;
    if ([fileManager fileExistsAtPath:path]) {
        NSEnumerator *childEnumber = [[fileManager subpathsOfDirectoryAtPath:path error:nil] objectEnumerator];
        NSString *fileName;
        while ((fileName = [childEnumber nextObject]) != nil) {
            NSString *childFilePath = [path stringByAppendingPathComponent:fileName];
            folderSize += [fileManager attributesOfItemAtPath:childFilePath error:nil].fileSize;
        }
    }
    
    NSLog(@"Cache size:%@",self.clearCacheItem.caption);
    
    self.clearCacheItem.caption = [NSString stringWithFormat:@"清空缓存（ %0.1f Mb）", (float)folderSize/1000/1000];
    self.atvDeviceIdField.text = AppDele.atvDeviceId;
    self.atvDeviceIdField.placeholder = AppDele.atvDeviceId;
    
    NSArray* cookies =[[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
    NSString* sessionid = @"N/A";
    NSString* lxsessionid = @"N/A";
    NSString* vip = @"否";
    NSString* userid = @"N/A";
    NSString* gdriveid = @"N/A";
    for(int i=0;i<[cookies count];i++){
        NSHTTPCookie* cookie = [cookies objectAtIndex:i];
        NSLog(@"%@ %@ %@",cookie.name,cookie.value,cookie.domain);
        if([cookie.domain rangeOfString:@".xunlei.com"].location!=NSNotFound){
            if([cookie.name isEqualToString:@"userid"]){
                userid = cookie.value;
            }else if([cookie.name isEqualToString:@"lsessionid"]){
                sessionid = cookie.value;
            }else if([cookie.name isEqualToString:@"isvip"]){
                vip = cookie.value;
                if([vip isEqualToString:@"6"]){
                    vip = @"是";
                }
            }else if([cookie.name isEqualToString:@"gdriveid"]){
                gdriveid=cookie.value;
            }else if([cookie.name isEqualToString:@"lx_sessionid"]){
                lxsessionid = cookie.value;
            }
        }
    }
    NSString* xunleiStatus = [NSString stringWithFormat:@"%@\nsessionid:%@\nuserid:%@\nvip:%@\ngdriveid:%@\nlxsessionid:%@",@"<strong>迅雷状态</strong>",sessionid,userid,vip,gdriveid,lxsessionid];
    self.xunleiStatusItem.text = [TTStyledText textFromXHTML:xunleiStatus lineBreaks:YES URLs:NO];
    _flags.isModelDidLoadInvalid = YES;
    [self invalidateView];

}
- (void)hudWasHidden:(MBProgressHUD *)hud {
    // Remove HUD from screen when the HUD was hidded
    [_progressHUD removeFromSuperview];
    [_progressHUD release];
    _progressHUD = nil;
}

- (void)loadView
{
    [super loadView];
}

#pragma mark -
#pragma mark TTModelViewController
- (void)createModel {
    NSMutableArray* items = [NSMutableArray array];
    
    [items addObject:@"服务器"];
    TTTableControlItem* serverIPItem = [TTTableControlItem itemWithCaption:@"服务器IP" control:self.serverIPField];
    [items addObject:serverIPItem];
    TTTableControlItem* atvDeviceIdItem = [TTTableControlItem itemWithCaption:@"ATV ID" control:self.atvDeviceIdField];
    [items addObject:atvDeviceIdItem];
    TTTableControlItem* syncItem = [TTTableControlItem itemWithCaption:@"更新脚本" control:self.syncButton];
    [items addObject:syncItem];
    self.refreshIPItem = [TTTableControlItem itemWithCaption:[NSString stringWithFormat:@"本机IP %@",AppDele.ipAddress] control:self.refreshIPButton];
    [items addObject:self.refreshIPItem];
    
    NSArray* cookies =[[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
    NSString* sessionid = @"N/A";
    NSString* lxsessionid = @"N/A";
    NSString* vip = @"否";
    NSString* userid = @"N/A";
    NSString* gdriveid = @"N/A";
    for(int i=0;i<[cookies count];i++){
        NSHTTPCookie* cookie = [cookies objectAtIndex:i];
        NSLog(@"%@ %@ %@",cookie.name,cookie.value,cookie.domain);
        if([cookie.domain rangeOfString:@".xunlei.com"].location!=NSNotFound){
            if([cookie.name isEqualToString:@"userid"]){
                userid = cookie.value;
            }else if([cookie.name isEqualToString:@"lsessionid"]){
                sessionid = cookie.value;
            }else if([cookie.name isEqualToString:@"isvip"]){
                vip = cookie.value;
                if([vip isEqualToString:@"6"]){
                    vip = @"是";
                }
            }else if([cookie.name isEqualToString:@"gdriveid"]){
                gdriveid=cookie.value;
            }else if([cookie.name isEqualToString:@"lx_sessionid"]){
                lxsessionid = cookie.value;
            }
        }
    }
    NSString* xunleiStatus = [NSString stringWithFormat:@"sessionid:%@\nuserid:%@\nvip:%@\ngdriveid:%@\nlxsessionid:%@",sessionid,userid,vip,gdriveid,lxsessionid];
    
    self.xunleiStatusItem = [TTTableTextItem itemWithText:xunleiStatus URL:nil];
    if(!AppDele.appleApproveCheck){
        [items addObject:@"迅雷"];
        [items addObject:[TTTableTextItem itemWithText:@"迅雷云播登录" URL:@"atvserver://xunlei/login/0"]];
        [items addObject:[TTTableTextItem itemWithText:@"迅雷离线登录" URL:@"atvserver://xunlei/login/1"]];
        [items addObject:self.xunleiStatusItem];
    }
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES);
    NSString *path = [[paths objectAtIndex:0] stringByAppendingPathComponent:@"/Three20/"];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    unsigned long long folderSize = 0;
    if ([fileManager fileExistsAtPath:path]) {
        NSEnumerator *childEnumber = [[fileManager subpathsOfDirectoryAtPath:path error:nil] objectEnumerator];
        NSString *fileName;
        while ((fileName = [childEnumber nextObject]) != nil) {
            NSString *childFilePath = [path stringByAppendingPathComponent:fileName];
            folderSize += [fileManager attributesOfItemAtPath:childFilePath error:nil].fileSize;
        }
    }
    
    self.clearCacheItem = [TTTableControlItem itemWithCaption:[NSString stringWithFormat:@"清空缓存（ %0.1f Mb）", (float)folderSize/1000/1000] control:self.clearCacheButton];
    self.networkSwitchy = [[[UISwitch alloc] init] autorelease];
    [self.networkSwitchy setOn:AppDele.ttgNetwork];
    [self.networkSwitchy addTarget:self action:@selector(changeNetwork:) forControlEvents:UIControlEventValueChanged];
    TTTableControlItem* networkItem = [TTTableControlItem itemWithCaption:@"2G/3G网络播放和下载" control:self.networkSwitchy];
    
    [items addObject:@"网络"];
    [items addObject:self.clearCacheItem];
    [items addObject:networkItem];
    
    NSString* clientVersion = @"未知";
    if(AppDele.clientVersion!=NULL&&AppDele.clientVersion.length>0){
        clientVersion = AppDele.clientVersion;
    }

    NSString* versionLabel = [NSString stringWithFormat:@"当前版本:%@ 最新版本:%@",ATV_CLIENT_VERSION,clientVersion];

     TTTableTextItem* versionItem = [TTTableTextItem itemWithText:versionLabel URL:@"http://clican.org/appletv/help.html"];
    [items addObject:@"版本"];
    [items addObject:versionItem];
    
    ConfigDataSource* ds = [ConfigDataSource dataSourceWithArrays:items];
    ds.callback = (ConfigCallback*)self;
    self.dataSource = ds;
    //self.tableView.delegate =self;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    if (textField==self.serverIPField) {
        if(textField.text==nil||textField.text.length==0){
            textField.text = ATV_SERVER_DEFAULT_IP;
        }
        [defaults setValue:textField.text forKey:ATV_SERVER_IP_NAME];
        AppDele.serverIP = textField.text;
    }else if(textField==self.atvDeviceIdField){
        if(textField.text==nil||textField.text.length==0){
            textField.text = [AtvUtil getUUID];
        }
        [defaults setValue:textField.text forKey:ATV_DEVICE_ID_NAME];
        AppDele.atvDeviceId = textField.text;
    }
    [textField resignFirstResponder];
    return YES;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if(indexPath.row==3){
        return 150;
    }else{
        return 60;
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    TTListDataSource* ds = (TTListDataSource*)tableView.dataSource;
    
    TTTableItem* item = [ds.items objectAtIndex:indexPath.row];
    if([item isKindOfClass:[TTTableLinkedItem class]]){
        TTTableLinkedItem* linkedItem = (TTTableLinkedItem*)item;
        if(linkedItem.URL!=nil&&linkedItem.URL.length>0){
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:linkedItem.URL]];
        }
    }

}

- (void)viewDidLoad
{
    [super viewDidLoad];
	NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    self.serverIPField.text = [defaults stringForKey: ATV_SERVER_IP_NAME];
    if(self.serverIPField.text==nil||self.serverIPField.text.length==0){
        self.serverIPField.text = ATV_SERVER_DEFAULT_IP;
    }
}

- (void)changeNetwork:(id)sender
{
    BOOL state = [sender isOn];
    AppDele.ttgNetwork = state;
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    [defaults setBool:AppDele.ttgNetwork  forKey:TTG_NETWORK_NAME];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
