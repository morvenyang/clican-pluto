//
//  PasswordViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-12-1.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "PasswordViewController.h"
#import "StyleSheet.h"
#import "AppDelegate.h"
@implementation PasswordViewController

@synthesize passwordField = _passwordField;
@synthesize confirmPasswordField = _confirmPasswordField;
@synthesize oldPasswordField = _oldPasswordField;
@synthesize titleImageView = _titleImageView;
@synthesize type = _type;
-(id) initWithType:(NSString*) type{
    self = [super init];
    if(self){
        self.type = type;
        [[TTNavigator navigator] removeAllViewControllers];
        
        _loginModel = [[LoginModel alloc] init];
        _loginModel.passwordDelegate = self;
        #ifdef __IPHONE_7_0
        self.edgesForExtendedLayout = UIRectEdgeNone;
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
    return nil;
}






- (void)loadView
{
    
    [super loadView];
    
    
    self.title = @"修改密码";
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
    CGFloat inputOffset = 40;
    if(IS_IPHONE6_PLUS){
        inputOffset = 60;
    }
    self.view.backgroundColor = [StyleSheet colorFromHexString:@"#EAEEF2"];
    
    UIImage* titleImage = [UIImage imageNamed:@"密码修改背景"];
    _titleImageView = [[TTImageView alloc] initWithFrame:CGRectMake((SCREEN_WIDTH-titleImage.size.width)/2,topOffset, titleImage.size.width, titleImage.size.height)];
    
    _titleImageView.defaultImage = titleImage;
    
    self.oldPasswordField.frame = CGRectMake((SCREEN_WIDTH-titleImage.size.width)/2+inputOffset,topOffset, titleImage.size.width-inputOffset, titleImage.size.height/3);
    self.passwordField.frame = CGRectMake((SCREEN_WIDTH-titleImage.size.width)/2+inputOffset,topOffset+titleImage.size.height/3, titleImage.size.width-inputOffset, titleImage.size.height/3);
    
    self.confirmPasswordField.frame = CGRectMake((SCREEN_WIDTH-titleImage.size.width)/2+inputOffset,topOffset+titleImage.size.height*2/3, titleImage.size.width-inputOffset, titleImage.size.height/3);
    
    self.submitButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    UIImage* submitButtonImage = [UIImage imageNamed:@"确认按钮"];

    self.submitButton.frame = CGRectMake((SCREEN_WIDTH-submitButtonImage.size.width)/2, topOffset+titleImage.size.height+diff, submitButtonImage.size.width, submitButtonImage.size.height);
    
    [self.submitButton setImage:submitButtonImage forState:UIControlStateNormal];
    [self.submitButton addTarget:self action:@selector(submitAction) forControlEvents: UIControlEventTouchUpInside];
    
    
    UIButton* cancelButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    UIImage* cancelButtonImage = [UIImage imageNamed:@"取消按钮"];

    cancelButton.frame = CGRectMake((SCREEN_WIDTH-cancelButtonImage.size.width)/2, topOffset+titleImage.size.height+diff+submitButtonImage.size.height+20, cancelButtonImage.size.width, cancelButtonImage.size.height);
    
    [cancelButton setImage:cancelButtonImage forState:UIControlStateNormal];
    [cancelButton addTarget:self action:@selector(cancelAction) forControlEvents: UIControlEventTouchUpInside];
    
    [self.view addSubview:_titleImageView];
    [self.view addSubview:self.oldPasswordField];
    [self.view addSubview:self.passwordField];
    [self.view addSubview:self.confirmPasswordField];
    [self.view addSubview:self.submitButton];
    [self.view addSubview:cancelButton];
}



- (void)dealloc
{
    _loginModel.delegate = nil;
    
    TT_RELEASE_SAFELY(_oldPasswordField);
    TT_RELEASE_SAFELY(_passwordField);
     TT_RELEASE_SAFELY(_confirmPasswordField);
    TT_RELEASE_SAFELY(_loginModel);
    TT_RELEASE_SAFELY(_titleImageView);
    TT_RELEASE_SAFELY(_submitButton);
    TT_RELEASE_SAFELY(_type);
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle



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
- (void) submitAction
{
    
    NSString* password= _passwordField.text;
    NSString* confirmPassword = _confirmPasswordField.text;
    if(password!=nil&&confirmPassword!=nil&&[password isEqualToString:confirmPassword]){
        _submitButton.enabled = false;
        NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
        
        [_loginModel changePassword:[defaults valueForKey:LAST_USER_NAME] password:_passwordField.text oldPassword:_oldPasswordField.text];
    }else{
        TTAlert(@"2次密码输入不一致");
    }
    
}
- (void) cancelAction
{
    if([self.type isEqualToString:@"reset"]){
        TTAlert(@"你必须修改密码后才能使用应用");
    }else{
        TTOpenURL(@"peacebird://index");
    }
}


#pragma mark -
#pragma mark Fields
- (UITextField *)oldPasswordField
{
    if (_oldPasswordField == nil)
    {
        _oldPasswordField = [[UITextField alloc] init];
        
        _oldPasswordField.font = [UIFont fontWithName:@"Microsoft YaHei" size:24];
        _oldPasswordField.textColor = [StyleSheet colorFromHexString:@"#a3a3a3"];
        _oldPasswordField.placeholder =
        @"请输入旧密码";
        
        _oldPasswordField.autocorrectionType = UITextAutocorrectionTypeNo;
        _oldPasswordField.keyboardType = UIKeyboardTypeDefault;
        _oldPasswordField.secureTextEntry =YES;
        _oldPasswordField.returnKeyType = UIReturnKeyDone;
        
        _oldPasswordField.clearButtonMode = UITextFieldViewModeAlways;
        _oldPasswordField.contentVerticalAlignment = UIControlContentHorizontalAlignmentCenter;
        _oldPasswordField.delegate = self;
    }
    return _oldPasswordField;
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



- (UITextField *)confirmPasswordField
{
    if (_confirmPasswordField == nil)
    {
        _confirmPasswordField = [[UITextField alloc] init];
        
        _confirmPasswordField.font = [UIFont fontWithName:@"Microsoft YaHei" size:24];
        _confirmPasswordField.textColor = [StyleSheet colorFromHexString:@"#a3a3a3"];
        _confirmPasswordField.placeholder =
        @"请再次输入密码";
        
        _confirmPasswordField.autocorrectionType = UITextAutocorrectionTypeNo;
        _confirmPasswordField.keyboardType = UIKeyboardTypeDefault;
        _confirmPasswordField.returnKeyType = UIReturnKeyDone;
        _confirmPasswordField.secureTextEntry=YES;
        
        _confirmPasswordField.clearButtonMode = UITextFieldViewModeAlways;
        _confirmPasswordField.contentVerticalAlignment = UIControlContentHorizontalAlignmentCenter;
        _confirmPasswordField.delegate = self;
    }
    return _confirmPasswordField;
}
#pragma mark -
#pragma mark UITextFieldDelegate
- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}



- (void)passwordResult:(NSString*) result success:(BOOL) success
{
    _submitButton.enabled =YES;
    if(success){
        NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
        [defaults setObject:_passwordField.text forKey:LAST_PASSWORD];
        if([self.type isEqualToString:@"reset"]){
            TTOpenURL(@"peacebird://gestureLock/setting");
        }else{
            TTOpenURL(@"peacebird://index");
        }
    }else{
        TTAlert(result);
    }
    
    
}





@end
