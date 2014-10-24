//


#import <UIKit/UIKit.h>
#import "LoginViewController.h"
#import "User.h"
#import "AppDelegate.h"
#import "MBProgressHUD.h"
#import "StyleSheet.h"
#import "CRNavigationBar.h"


@implementation LoginViewController


@synthesize usernameField=_usernameField;
@synthesize passwordField=_passwordField;
@synthesize loginButton=_loginButton;
@synthesize titleImageView=_titleImageView;
@synthesize progressHUD = _progressHUD;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if ((self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil])) {
        [[TTNavigator navigator] removeAllViewControllers];
        
        _loginModel = [[LoginModel alloc] init];
        _loginModel.delegate = self;
        
    }
    
    
    #ifdef __IPHONE_7_0
        if(DEVICE_VERSION>=7.0){
        UIColor *navigationTextColor = [UIColor whiteColor];
        DrAppDelegate.window.tintColor = navigationTextColor;
        [[UINavigationBar appearance] setTitleTextAttributes:@{
                         NSForegroundColorAttributeName : navigationTextColor
         }];
        }
    #endif

    
    return self;
}

- (id)init {
    if ((self = [self initWithNibName:nil bundle:nil])) {
    }
    return self;
}


- (void)loadView
{
    
    [super loadView];

    
    self.title = @"登录";
    #ifdef __IPHONE_7_0
        if(DEVICE_VERSION>=7.0){
            self.navigationController.navigationBar.barTintColor=[UIColor blackColor];
        }else{
            self.navigationController.navigationBar.tintColor = [UIColor blackColor];
        }
    #else
        self.navigationController.navigationBar.tintColor = [UIColor blackColor];
    #endif
    NSLog(@"%f",SCREEN_WIDTH);
    CGFloat topOffset = SCREEN_HEIGHT/5;
    CGFloat diff= SCREEN_HEIGHT/12;
    self.view.backgroundColor = [StyleSheet colorFromHexString:@"#EAEEF2"];
    
    UIImage* titleImage = [UIImage imageNamed:@"用户名密码背景.png"];
    if(DEVICE_VERSION>=7.0){
        _titleImageView = [[TTImageView alloc] initWithFrame:CGRectMake((SCREEN_WIDTH-titleImage.size.width)/2,64+topOffset, titleImage.size.width, titleImage.size.height)];
    }else{
        _titleImageView = [[TTImageView alloc] initWithFrame:CGRectMake((SCREEN_WIDTH-titleImage.size.width)/2,topOffset, titleImage.size.width, titleImage.size.height)];
    }
    
    _titleImageView.defaultImage = titleImage;

    if(DEVICE_VERSION>=7.0){
        self.usernameField.frame = CGRectMake((SCREEN_WIDTH-titleImage.size.width)/2+40,topOffset+64, titleImage.size.width-40, titleImage.size.height/2);
        self.passwordField.frame = CGRectMake((SCREEN_WIDTH-titleImage.size.width)/2+40,topOffset+titleImage.size.height/2+64, titleImage.size.width-40, titleImage.size.height/2);
    }else{
        self.usernameField.frame = CGRectMake((SCREEN_WIDTH-titleImage.size.width)/2+40,topOffset, titleImage.size.width-40, titleImage.size.height/2);
        self.passwordField.frame = CGRectMake((SCREEN_WIDTH-titleImage.size.width)/2+40,topOffset+titleImage.size.height/2, titleImage.size.width-40, titleImage.size.height/2);
    }
    
    
    self.loginButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    UIImage* loginButtonImage = [UIImage imageNamed:@"登录按钮.png"];
    
    if(DEVICE_VERSION>=7.0){
        self.loginButton.frame = CGRectMake((SCREEN_WIDTH-loginButtonImage.size.width)/2, topOffset+titleImage.size.height+diff+64, loginButtonImage.size.width, loginButtonImage.size.height);
    }else{
        self.loginButton.frame = CGRectMake((SCREEN_WIDTH-loginButtonImage.size.width)/2, topOffset+titleImage.size.height+diff, loginButtonImage.size.width, loginButtonImage.size.height);
    }
    
    [self.loginButton setImage:[UIImage imageNamed:@"登录按钮.png"] forState:UIControlStateNormal];
    [self.loginButton addTarget:self action:@selector(loginAction) forControlEvents: UIControlEventTouchUpInside];
    
    [self.view addSubview:_titleImageView];
    [self.view addSubview:self.usernameField];
    [self.view addSubview:self.passwordField];
    [self.view addSubview:self.loginButton];
}



- (void)dealloc
{
    _loginModel.delegate = nil;
    
    TT_RELEASE_SAFELY(_usernameField);
    TT_RELEASE_SAFELY(_passwordField);  
    TT_RELEASE_SAFELY(_loginModel); 
    TT_RELEASE_SAFELY(_titleImageView);
    TT_RELEASE_SAFELY(_loginButton);
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    
    self.usernameField.text = [defaults stringForKey: LAST_USER_NAME];
    //self.passwordField.text = [defaults stringForKey: LAST_PASSWORD];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;

    //TT_RELEASE_SAFELY(_usernameField);
    //TT_RELEASE_SAFELY(_passwordField);  
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
    //return YES;
}





#pragma mark -
#pragma mark Actions;
- (void) loginAction
{
    _loginButton.enabled = NO;
    [_loginModel login:_usernameField.text password:_passwordField.text];
}



#pragma mark -
#pragma mark Fields
- (UITextField *)usernameField
{
	if (_usernameField == nil)
	{
        _usernameField = [[UITextField alloc] init];
        
        _usernameField.font = [UIFont fontWithName:@"Microsoft YaHei" size:24];
        _usernameField.textColor = [StyleSheet colorFromHexString:@"#a3a3a3"];
        _usernameField.placeholder =
        @"请输入用户名";
        
		_usernameField.autocorrectionType = UITextAutocorrectionTypeNo;	
        _usernameField.keyboardType = UIKeyboardTypeDefault;	
		_usernameField.returnKeyType = UIReturnKeyDone;
		
		_usernameField.clearButtonMode = UITextFieldViewModeAlways;
        _usernameField.contentVerticalAlignment = UIControlContentHorizontalAlignmentCenter;
		_usernameField.delegate = self;
	}	
	return _usernameField;
}

- (UITextField *)passwordField
{
	if (_passwordField == nil)
	{
        _passwordField = [[UITextField alloc] init];
        _passwordField.font = [UIFont fontWithName:@"Microsoft YaHei" size:24];
        _passwordField.textColor = [StyleSheet colorFromHexString:@"#a3a3a3"];        _passwordField.placeholder =@"请输入密码";
        
		_passwordField.autocorrectionType = UITextAutocorrectionTypeNo;	
        _passwordField.keyboardType = UIKeyboardTypeDefault;	
		_passwordField.returnKeyType = UIReturnKeyDone;
		_passwordField.secureTextEntry = TRUE;
        
		_passwordField.clearButtonMode = UITextFieldViewModeAlways;
        _passwordField.contentVerticalAlignment = UIControlContentHorizontalAlignmentCenter;
		_passwordField.delegate = self;		

	}	
	return _passwordField;
}




#pragma mark -
#pragma mark UITextFieldDelegate
- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}

#pragma mark -
#pragma mark LoginDelegate

- (void)loginStart:(User*) user
{
    self.progressHUD = [[[MBProgressHUD alloc] initWithView:self.view] autorelease];
    self.progressHUD.delegate = self;
    self.progressHUD.labelText = @"登录中...";
    [self.view addSubview:self.progressHUD];
    [self.view bringSubviewToFront:self.progressHUD];
    [self.progressHUD show:YES];

}

- (void)loginSuccess:(User*) user
{
    [self.progressHUD hide:NO];
    _loginButton.enabled = YES;
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    
    [defaults setObject:user.username forKey:LAST_USER_NAME];
    [defaults setObject:user.password forKey:LAST_PASSWORD];
    NSDate* now = [NSDate date];
    NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setTimeStyle:NSDateFormatterFullStyle];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    
    [defaults setObject:[dateFormatter stringFromDate:now] forKey:LAST_LOGIN_DATE];
    [[TTNavigator navigator] removeAllViewControllers];
    DrAppDelegate.user = user;
    TTOpenURL(@"peacebird://index");
}
- (void)loginFailed:(NSError*) error message:(NSString*) message
{
    [self.progressHUD hide:NO];
    _loginButton.enabled = YES;
    //-1004 connection is not available
    //-1001 timeout
    if([error code]==-1004||[error code]==-1001){
        TTAlert(@"请检查网络链接");
    }else{
        _loginButton.enabled = YES;
        if(message){
            TTAlert(message);
        }else{
            TTAlert([error localizedDescription]);
        }
    }
    
}



#pragma mark -
#pragma mark MBProgressHUDDelegate methods

#pragma mark -
#pragma mark MBProgressHUDDelegate methods

- (void)hudWasHidden:(MBProgressHUD *)hud {
    // Remove HUD from screen when the HUD was hidded
    [_progressHUD removeFromSuperview];
    [_progressHUD release];
    _progressHUD = nil;
}


@end
