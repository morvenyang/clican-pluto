//
//  KKViewController.m
//  KKGestureLockView
//
//  Created by Luke on 8/5/13.
//  Copyright (c) 2013 geeklu. All rights reserved.
//

#import "KKViewController.h"
@interface KKViewController ()

@end

@implementation KKViewController

-(id) initWithType:(NSString*) type{
    self = [super init];
    if(self){
        self.type = type;
        self.time = 1;
        return self;
    }
    return nil;
}

-(void)loadView{
    [super loadView];
    self.navigationController.navigationBarHidden =YES;
    self.titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 100, 320, 20)];
    self.titleLabel.textAlignment =
    
    #ifdef __IPHONE_6_0
    self.titleLabel.textAlignment =NSTextAlignmentCenter;
    #else
    self.titleLabel.textAlignment =UITextAlignmentCenter;
    #endif
    if([self.type isEqualToString:@"unlock"]){
        self.titleLabel.text = @"请输入手势密码";
    }else{
        self.titleLabel.text = @"请设置手势密码";
    }
    self.lockView = [[KKGestureLockView alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    // Do any additional setup after loading the view, typically from a nib.
    self.view.backgroundColor = [UIColor clearColor];
    self.lockView.normalGestureNodeImage = [UIImage imageNamed:@"gesture_node_normal.png"];
    self.lockView.selectedGestureNodeImage = [UIImage imageNamed:@"gesture_node_selected.png"];
    self.lockView.lineColor = [[UIColor orangeColor] colorWithAlphaComponent:0.5];
    self.lockView.lineWidth = 12;
    self.lockView.delegate = self;
    self.lockView.contentInsets = UIEdgeInsetsMake(150, 20, 100, 20);
    
    [self.view addSubview:self.lockView];
    [self.view addSubview:self.titleLabel];
    [self addFootButtons];
}


-(void)addFootButtons{
    UIButton* forgetButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [forgetButton setTitle:@"忘记手势密码" forState:UIControlStateNormal];
    [forgetButton addTarget:self action:@selector(forget) forControlEvents:UIControlEventTouchUpInside];
    [forgetButton.titleLabel setFont:[UIFont systemFontOfSize:12]];
    forgetButton.frame = CGRectMake(30, self.view.frame.size.height-50, 120, 20);
    UIButton* changeButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [changeButton setTitle:@"用其他账户登录" forState:UIControlStateNormal];
    [changeButton addTarget:self action:@selector(change) forControlEvents:UIControlEventTouchUpInside];
    [changeButton.titleLabel setFont:[UIFont systemFontOfSize:12]];
    changeButton.frame = CGRectMake(self.view.frame.size.width-150, self.view.frame.size.height-50, 120, 20);
    [self.view addSubview:forgetButton];
    [self.view addSubview:changeButton];
}

-(void)forget{
    TTOpenURL(@"peacebird://login");
}

-(void)change{
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    [defaults removeObjectForKey:LAST_USER_NAME];
    TTOpenURL(@"peacebird://login");
}

-(void)viewDidLoad{
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor clearColor];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void)gestureLockView:(KKGestureLockView *)gestureLockView didBeginWithPasscode:(NSString *)passcode{
    NSLog(@"%@",passcode);
}

- (void)gestureLockView:(KKGestureLockView *)gestureLockView didEndWithPasscode:(NSString *)passcode{
    NSLog(@"%@",passcode);
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    if([self.type isEqualToString:@"unlock"]){
        NSString* gesturePassword = [defaults objectForKey:GESTURE_PASSWORD];
        if(![passcode isEqualToString:gesturePassword]){
            self.titleLabel.text = @"手势密码错误,请重新输入";
            self.titleLabel.textColor = [UIColor redColor];
        }else{
            self.navigationController.navigationBarHidden= NO;
            TTOpenURL(@"peacebird://index");
        }
    }else{
        if(self.time==1){
            self.password = passcode;
            self.time =2;
        }else{
            self.confirmedPassword = passcode;
            if([self.password isEqualToString:self.confirmedPassword]){
                [defaults setObject:self.password forKey:GESTURE_PASSWORD];
                self.navigationController.navigationBarHidden= NO;
                TTOpenURL(@"peacebird://index");
            }else{
                self.titleLabel.text = @"手势密码设置错误,请重新设置";
                self.titleLabel.textColor = [UIColor redColor];
                self.time =1;
            }
        }
    }
}

@end
