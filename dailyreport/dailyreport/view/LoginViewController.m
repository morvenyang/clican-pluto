//


#import <UIKit/UIKit.h>
#import "LoginViewController.h"
#import "User.h"
#import "AppDelegate.h"
#import "MBProgressHUD.h"

@implementation LoginViewController


@synthesize usernameField=_usernameField;
@synthesize passwordField=_passwordField;
@synthesize loginButton=_loginButton;
@synthesize titleImageView=_titleImageView;


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if ((self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil])) {
        [[TTNavigator navigator] removeAllViewControllers];
        
        self.tableViewStyle = UITableViewStyleGrouped;
        
        _loginModel = [[LoginModel alloc] init];
        _loginModel.delegate = self;
        
    }
    
    return self;
}

- (id)init {
    if ((self = [self initWithNibName:nil bundle:nil])) {
    }
    return self;
}


- (void)loadView
{
    self.navigationController.navigationBarHidden = YES;
    [super loadView];
    CGRect bounds = [UIScreen mainScreen].bounds;
    //self.tableView.frame = CGRectMake(20, 125, 280, 80);
    UIView* backgroundView = [[[UIView alloc] autorelease] initWithFrame:self.view.frame];
    //backgroundView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"login_pageBg.png"]];
    
    self.tableView.backgroundView = backgroundView;
    self.tableView.backgroundColor = [UIColor clearColor];
    UIImage* titleImage = [UIImage imageNamed:@"login_title.png"];
    
    [self.tableView setContentInset:UIEdgeInsetsMake(125+titleImage.size.height,0,0,0)];
    
    
    _titleImageView = [[TTImageView alloc] initWithFrame:CGRectMake((bounds.size.width-titleImage.size.width)/2, 110, 0, 0)];
    _titleImageView.defaultImage = titleImage;
    _titleImageView.contentMode = UIViewContentModeScaleToFill;
    
    
    self.loginButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    UIImage* loginButtonImage = [UIImage imageNamed:@"button_login.png"];
    self.loginButton.frame = CGRectMake(24, 27+125+titleImage.size.height+80, loginButtonImage.size.width, loginButtonImage.size.height);
    [self.loginButton setImage:[UIImage imageNamed:@"button_login.png"] forState:UIControlStateNormal];
    [self.loginButton addTarget:self action:@selector(loginAction) forControlEvents: UIControlEventTouchUpInside];
    
    [self.view addSubview:_titleImageView];
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
#pragma mark TTModelViewController
- (void)createModel {
    self.dataSource = [LoginDataSource dataSourceWithObjects:
                       @"",
                       [TTTableControlItem itemWithCaption:@"用户名" control:self.usernameField],
                       [TTTableControlItem itemWithCaption:@"密码" control:self.passwordField],
                       nil];    
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
        
        _usernameField.font = [UIFont fontWithName:@"Microsoft YaHei" size:14];
        _usernameField.textColor = RGBCOLOR(204,204,204);
        _usernameField.placeholder =
        @"用户名";
        
		_usernameField.autocorrectionType = UITextAutocorrectionTypeNo;	
        _usernameField.keyboardType = UIKeyboardTypeDefault;	
		_usernameField.returnKeyType = UIReturnKeyDone;
		
		_usernameField.clearButtonMode = UITextFieldViewModeAlways;	
		_usernameField.delegate = self;
	}	
	return _usernameField;
}

- (UITextField *)passwordField
{
	if (_passwordField == nil)
	{
        _passwordField = [[UITextField alloc] init];
        _passwordField.font = [UIFont fontWithName:@"Microsoft YaHei" size:14];
        _passwordField.textColor = RGBCOLOR(204,204,204);
        _passwordField.placeholder =@"密码";
        
		_passwordField.autocorrectionType = UITextAutocorrectionTypeNo;	
        _passwordField.keyboardType = UIKeyboardTypeDefault;	
		_passwordField.returnKeyType = UIReturnKeyDone;
		_passwordField.secureTextEntry = TRUE;
        
		_passwordField.clearButtonMode = UITextFieldViewModeAlways;		
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
    
    
    
    
}

- (void)loginSuccess:(User*) user
{
    [HUD hide:NO];
    _loginButton.enabled = YES;
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    
    [defaults setObject:user.username forKey:LAST_USER_NAME];
    [[TTNavigator navigator] removeAllViewControllers];
    DrAppDelegate.user = user;
    TTOpenURL(@"peacebird://index");
}
- (void)loginFailed:(NSError*) error message:(NSString*) message
{
    [HUD hide:NO];
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

- (void)hudWasHidden:(MBProgressHUD *)hud {
	NSLog(@"Hud: %@", hud);
    // Remove HUD from screen when the HUD was hidded
    [HUD removeFromSuperview];
    [HUD release];
}


#pragma mark -

@end
