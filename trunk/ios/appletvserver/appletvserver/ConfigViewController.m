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

@implementation ConfigViewController

@synthesize serverIPField = _serverIPField;
@synthesize syncButton = _syncButton;
@synthesize progressHUD = _progressHUD;
@synthesize clearCacheButton = _clearCacheButton;
@synthesize clearCacheItem = _clearCacheItem;
@synthesize atvDeviceIdField = _atvDeviceIdField;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"设置";
        self.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"设置" image:nil tag:3] autorelease];
        
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
    
    [super dealloc];
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
    
    
    TTTableControlItem* serverIPItem = [TTTableControlItem itemWithCaption:@"服务器IP" control:self.serverIPField];
    [items addObject:serverIPItem];
    TTTableControlItem* atvDeviceIdItem = [TTTableControlItem itemWithCaption:@"ATV ID" control:self.atvDeviceIdField];
    [items addObject:atvDeviceIdItem];
    TTTableControlItem* syncItem = [TTTableControlItem itemWithCaption:@"更新脚本" control:self.syncButton];
    [items addObject:syncItem];
    
    NSArray* cookies =[[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
    NSString* sessionid = @"N/A";
    NSString* vip = @"否";
    NSString* userid = @"N/A";
    for(int i=0;i<[cookies count];i++){
        NSHTTPCookie* cookie = [cookies objectAtIndex:i];
        NSLog(@"%@ %@ %@",cookie.name,cookie.value,cookie.domain);
        if([cookie.domain isEqualToString:@".xunlei.com"]){
            if([cookie.name isEqualToString:@"userid"]){
                userid = cookie.value;
            }else if([cookie.name isEqualToString:@"lsessionid"]){
                sessionid = cookie.value;
            }else if([cookie.name isEqualToString:@"isvip"]){
                vip = cookie.value;
                if([vip isEqualToString:@"6"]){
                    vip = @"是";
                }
            }
        }
    }
    NSString* xunleiStatus = [NSString stringWithFormat:@"%@\nsessionid:%@\nuserid:%@\nvip:%@",@"<strong>迅雷状态</strong>",sessionid,userid,vip];
    
    TTTableStyledTextItem* xunleiStatusItem = [TTTableStyledTextItem itemWithText:[TTStyledText textFromXHTML:xunleiStatus lineBreaks:YES URLs:NO] URL:nil];

    [items addObject:xunleiStatusItem];
    
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

    [items addObject:self.clearCacheItem];
    
    

    
    ConfigDataSource* ds = [[[ConfigDataSource alloc] initWithItems:items callback:self] autorelease];
    self.dataSource = ds;
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
            textField.text = [[UIDevice currentDevice] uniqueIdentifier];
        }
        [defaults setValue:textField.text forKey:ATV_DEVICE_ID_NAME];
        AppDele.atvDeviceId = textField.text;
    }
    [textField resignFirstResponder];
    return YES;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if(indexPath.row==2){
        return 150;
    }else{
        return 60;
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

- (id<UITableViewDelegate>)createDelegate {
    return [[[TTTableViewDragRefreshDelegate alloc] initWithController:self] autorelease];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
