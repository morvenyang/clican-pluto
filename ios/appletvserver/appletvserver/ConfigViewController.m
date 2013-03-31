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

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"设置";
        self.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"设置" image:nil tag:3] autorelease];
        self.serverIPField = [[[UITextField alloc] init] autorelease];
        self.serverIPField.font = [UIFont fontWithName:@"Microsoft YaHei" size:14];
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
    }
    return self;
}

-(void) dealloc{
    TT_RELEASE_SAFELY(_serverIPField);
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

- (void) syncScript:(id)object{
    NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
    [[AppDele webContentSync] syncWebContent:self.progressHUD force:YES];
    [[AppDele jsEngine] reloadJS];
    [self.progressHUD hide:YES];
    [pool release];
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
    
    TTTableControlItem* syncItem = [TTTableControlItem itemWithCaption:@"更新脚本" control:self.syncButton];
    [items addObject:syncItem];
    
    TTListDataSource* ds = [[TTListDataSource alloc] initWithItems:items];
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
    }
    [textField resignFirstResponder];
    return YES;
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

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
