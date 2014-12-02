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
    self.titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, SCREEN_HEIGHT/4, SCREEN_WIDTH, 20)];
    
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
    int fontSize = 12;
    if(IS_IPHONE6){
        fontSize = 14;
    }else if (IS_IPHONE6_PLUS){
        fontSize = 16;
    }
    self.titleLabel.font =[UIFont systemFontOfSize:fontSize];
    self.titleLabel.textColor = [UIColor whiteColor];
    self.lockView = [[KKGestureLockView alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    // Do any additional setup after loading the view, typically from a nib.
    self.view.backgroundColor = [UIColor clearColor];
    self.lockView.normalGestureNodeImage = [UIImage imageNamed:@"gesture_node_normal"];
    self.lockView.selectedGestureNodeImage = [UIImage imageNamed:@"gesture_node_selected"];
    self.lockView.lineColor = [[UIColor orangeColor] colorWithAlphaComponent:0.5];
    self.lockView.lineWidth = 12;
    self.lockView.delegate = self;
    self.lockView.contentInsets = UIEdgeInsetsMake(SCREEN_HEIGHT/3, 20, 100, 20);
    
    [self.view addSubview:self.lockView];
    [self.view addSubview:self.titleLabel];
    [self addFootButtons];
}
-(void)viewWillAppear:(BOOL)animated{
    self.time = 1;
}

-(void)addFootButtons{
    int fontSize = 12;
    if(IS_IPHONE6){
        fontSize = 14;
    }else if (IS_IPHONE6_PLUS){
        fontSize = 16;
    }
    UIButton* forgetButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [forgetButton setTitle:@"忘记手势密码" forState:UIControlStateNormal];
    [forgetButton addTarget:self action:@selector(forget) forControlEvents:UIControlEventTouchUpInside];
    [forgetButton.titleLabel setFont:[UIFont systemFontOfSize:fontSize]];
    forgetButton.frame = CGRectMake(30, self.view.frame.size.height-50, 120, 20);
    UIButton* changeButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [changeButton setTitle:@"用其他账户登录" forState:UIControlStateNormal];
    [changeButton addTarget:self action:@selector(change) forControlEvents:UIControlEventTouchUpInside];
    [changeButton.titleLabel setFont:[UIFont systemFontOfSize:fontSize]];
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
        if(self.time>5){
            self.titleLabel.text = @"超过最大重试次数，请选择忘记手势密码或重新登录";
            self.titleLabel.textColor = [UIColor redColor];
        }else{
            if(![passcode isEqualToString:gesturePassword]){
                if(self.time==5){
                    self.titleLabel.text = @"手势密码错误，请选择忘记手势密码或重新登录";
                    self.titleLabel.textColor = [UIColor redColor];
                    self.time++;
                    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
                    [defaults removeObjectForKey:GESTURE_PASSWORD];
                }else{
                    self.titleLabel.text = [NSString stringWithFormat:@"手势密码错误，还可以输入%i次",5-self.time];
                    self.titleLabel.textColor = [UIColor redColor];
                    self.time++;
                }

            }else{
                self.navigationController.navigationBarHidden= NO;
                TTOpenURL(@"peacebird://index");
            }
        }
        
    }else{
        if(self.time==1){
            self.password = passcode;
            self.titleLabel.text = @"请再次设置以确认手势密码";
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
