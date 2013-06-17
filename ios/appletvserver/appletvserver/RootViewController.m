//
//  RootViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-3-12.
//
//

#import "RootViewController.h"
#import "AppDelegate.h"
#import "MainViewController.h"
#import "Constants.h"
#import "UITabBarControllerAdditions.h"
@implementation RootViewController

@synthesize progressHUD = _progressHUD;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        [[AppDele scriptRefreshDelegateArray] addObject:self];
    }
    return self;
}

-(void) refreshScript{
    [[AppDele jsEngine] reloadJS];
    NSString* check =[[AppDele jsEngine] runJS:@"appletv.isAppleApproveCheck();"];
    if(check!=nil&&[check isEqualToString:@"false"]){
        AppDele.appleApproveCheck = FALSE;
    }else{
        AppDele.appleApproveCheck = TRUE;
    }
    if(AppDele.appleApproveCheck){
        [self setTabURLs:[NSArray arrayWithObjects:
                          @"atvserver://main",
                          @"atvserver://download",
                          @"atvserver://config",
                          nil]];
    }else{
        [self setTabURLs:[NSArray arrayWithObjects:
                          @"atvserver://main",
                          @"atvserver://download",
                          @"atvserver://localdownload",
                          @"atvserver://smb/auth",
                          @"atvserver://config",
                          nil]];
    }
}

- (void)loadView
{
    [super loadView];
    self.progressHUD = [[[MBProgressHUD alloc] initWithView:self.view] autorelease];
    self.progressHUD.delegate = self;
    self.progressHUD.labelText = @"加载脚本中...";
    [self.view addSubview:self.progressHUD];
    [self.view bringSubviewToFront:self.progressHUD];
    [self.progressHUD show:YES];
    
}

- (void)viewDidLoad
{
    NSLog(@"RootViewController viewDidLoad");
    
    [super viewDidLoad];
    [NSThread detachNewThreadSelector:@selector(afterViewLoaded:) toTarget:self withObject:nil];
}

- (void) afterViewLoaded:(id)object{
    NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
    BOOL result = [[AppDele webContentSync] syncWebContent:self.progressHUD force:NO];
    
    [[AppDele jsEngine] reloadJS];
    
    [AppDele initProcess];
    [self.progressHUD hide:YES];
    if(!result){
        [self performSelectorOnMainThread:@selector(showNetworkWarning) withObject:nil waitUntilDone:NO];
    }
    [self showTabs];
    [pool release];
}

-(void)showTabs{
    NSString* check =[[AppDele jsEngine] runJS:@"appletv.isAppleApproveCheck();"];
    if(check!=nil&&[check isEqualToString:@"false"]){
        AppDele.appleApproveCheck = FALSE;
    }else{
        AppDele.appleApproveCheck = TRUE;
    }
    if(AppDele.appleApproveCheck){
        [self setTabURLs:[NSArray arrayWithObjects:
                          @"atvserver://main",
                          @"atvserver://download",
                          @"atvserver://config",
                          nil]];
    }else{
        [self setTabURLs:[NSArray arrayWithObjects:
                          @"atvserver://main",
                          @"atvserver://download",
                          @"atvserver://localdownload",
                          @"atvserver://smb/auth",
                          @"atvserver://config",
                          nil]];
    }
}
-(void)showNetworkWarning{
    TTAlert(@"加载脚本失败,请检查网络是否正常连接");
}

#pragma mark -
#pragma mark MBProgressHUDDelegate methods

- (void)hudWasHidden:(MBProgressHUD *)hud {
    // Remove HUD from screen when the HUD was hidded
    [_progressHUD removeFromSuperview];
    [_progressHUD release];
    _progressHUD = nil;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
