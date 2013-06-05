//
//  SmbAuthViewController.m
//  kxsmb project
//  https://github.com/kolyvan/kxsmb/
//
//  Created by Kolyvan on 29.03.13.
//

/*
 Copyright (c) 2013 Konstantin Bukreev All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 
 - Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 
 - Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/


#import "SmbAuthViewController.h"
#import "KxSMBProvider.h"
#import "Constants.h"
#import "AppDelegate.h"
@interface SmbAuthViewController ()
@end

@implementation SmbAuthViewController {

    UITextField *_pathField;
    UITextField *_usernameField;
    UITextField *_passwordField;
}


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title =  @"共享";
        self.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"共享" image:nil tag:3];
    }
    return self;
}

- (void) loadView
{
    CGRect frame = [[UIScreen mainScreen] applicationFrame];    
    const CGFloat W = frame.size.width;
    const CGFloat H = frame.size.height;
    
    self.view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, W, H)];
    self.view.backgroundColor = [UIColor whiteColor];
    

    UILabel *pathLabel;
    pathLabel = [[UILabel alloc] initWithFrame:CGRectMake(10,10,90,30)];
    pathLabel.backgroundColor = [UIColor clearColor];
    pathLabel.textColor = [UIColor darkTextColor];
    pathLabel.font = [UIFont systemFontOfSize:16];
    pathLabel.text = NSLocalizedString(@"服务器IP", nil);
    [self.view addSubview:pathLabel];
    
    UILabel *usernameLabel;
    usernameLabel = [[UILabel alloc] initWithFrame:CGRectMake(10,50,90,30)];
    usernameLabel.backgroundColor = [UIColor clearColor];
    usernameLabel.textColor = [UIColor darkTextColor];
    usernameLabel.font = [UIFont boldSystemFontOfSize:16];
    usernameLabel.text = @"用户名";
    [self.view addSubview:usernameLabel];
    
    UILabel *passwordLabel;
    passwordLabel = [[UILabel alloc] initWithFrame:CGRectMake(10,90,90,30)];
    passwordLabel.backgroundColor = [UIColor clearColor];
    passwordLabel.textColor =  [UIColor darkTextColor];
    passwordLabel.font = [UIFont boldSystemFontOfSize:16];
    passwordLabel.text = @"密码";
    [self.view addSubview:passwordLabel];
    
    _pathField = [[UITextField alloc] initWithFrame:CGRectMake(100,10,W-110,30)];
    _pathField.autocapitalizationType = UITextAutocapitalizationTypeNone;
    _pathField.autocorrectionType = UITextAutocorrectionTypeNo;
    _pathField.spellCheckingType = UITextSpellCheckingTypeNo;
    _pathField.autoresizingMask = UIViewAutoresizingFlexibleWidth;
    _pathField.clearButtonMode =  UITextFieldViewModeWhileEditing;
    _pathField.textColor = [UIColor blueColor];
    _pathField.font = [UIFont systemFontOfSize:16];
    _pathField.borderStyle = UITextBorderStyleRoundedRect;
    _pathField.backgroundColor = [UIColor lightGrayColor];
    _pathField.returnKeyType = UIReturnKeyNext;
    
    [_pathField addTarget:self
                        action:@selector(textFieldDoneEditing:)
              forControlEvents:UIControlEventEditingDidEndOnExit];
    
    [self.view addSubview:_pathField];
    
    
    _usernameField = [[UITextField alloc] initWithFrame:CGRectMake(100, 50, W - 110, 30)];
    _usernameField.autocapitalizationType = UITextAutocapitalizationTypeNone;
    _usernameField.autocorrectionType = UITextAutocorrectionTypeNo;
    _usernameField.spellCheckingType = UITextSpellCheckingTypeNo;
    _usernameField.autoresizingMask = UIViewAutoresizingFlexibleWidth;
    _usernameField.clearButtonMode =  UITextFieldViewModeWhileEditing;
    _usernameField.textColor = [UIColor blueColor];
    _usernameField.font = [UIFont systemFontOfSize:16];
    _usernameField.borderStyle = UITextBorderStyleRoundedRect;
    _usernameField.backgroundColor = [UIColor lightGrayColor];
    _usernameField.returnKeyType = UIReturnKeyDone;
    
    [_usernameField addTarget:self
                   action:@selector(textFieldDoneEditing:)
         forControlEvents:UIControlEventEditingDidEndOnExit];
    
    [self.view addSubview:_usernameField];
    
    _passwordField = [[UITextField alloc] initWithFrame:CGRectMake(100, 90, W - 110, 30)];
    _passwordField.autocapitalizationType = UITextAutocapitalizationTypeNone;
    _passwordField.autocorrectionType = UITextAutocorrectionTypeNo;
    _passwordField.spellCheckingType = UITextSpellCheckingTypeNo;
    _passwordField.autoresizingMask = UIViewAutoresizingFlexibleWidth;
    _passwordField.clearButtonMode =  UITextFieldViewModeWhileEditing;
    _passwordField.textColor = [UIColor blueColor];
    _passwordField.font = [UIFont systemFontOfSize:16];
    _passwordField.borderStyle = UITextBorderStyleRoundedRect;
    _passwordField.backgroundColor = [UIColor lightGrayColor];
    _passwordField.returnKeyType = UIReturnKeyDone;
    _passwordField.secureTextEntry = YES;    
    
    [_passwordField addTarget:self
                       action:@selector(textFieldDoneEditing:)
             forControlEvents:UIControlEventEditingDidEndOnExit];
    
    [self.view addSubview:_passwordField];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    UIBarButtonItem *bbi;
    
    bbi = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone
                                                        target:self
                                                        action:@selector(doneAction)];
    bbi.title = @"连接";
    self.navigationItem.rightBarButtonItem = bbi;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    if(AppDele.auth!=nil){
        _server = AppDele.auth.serverIP;
        _username = AppDele.auth.username;
        _password = AppDele.auth.password;
    }
    _pathField.text = _server;
    _usernameField.text = _username;
    _passwordField.text = _password;
}

- (void) textFieldDoneEditing: (id) sender
{
}

- (void) doneAction
{
    _username = _usernameField.text;
    _password = _passwordField.text;
    _server = _pathField.text;
    AppDele.auth = [KxSMBAuth smbAuthWorkgroup:nil
                                         username:_username
                                         password:_password
                    serverIP:_server];
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    NSData *myEncodedObject = [NSKeyedArchiver archivedDataWithRootObject:AppDele.auth];
    [defaults setValue:myEncodedObject forKey:SMB_AUTH_NAME];
    NSString* url = [NSString stringWithFormat:@"atvserver://smb/%@",_server];
    TTOpenURL(url);
}

@end
