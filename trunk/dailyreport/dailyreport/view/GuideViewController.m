//
//  GuideViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-12-1.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "GuideViewController.h"
#import "Constants.h"
@implementation GuideViewController

@synthesize imageView = _imageView;
@synthesize index = _index;
@synthesize start = _start;

-(id) init{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.index = 0;
    }
    return self;
}

- (void)loadView
{
    [super loadView];
    self.navigationController.navigationBarHidden = YES;
    [[UIApplication sharedApplication] setStatusBarHidden:YES];
    self.imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.imageView.image = [UIImage imageNamed:@"首次进入提示"];
    
    UISwipeGestureRecognizer* swipeGestureRight = [[[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleGesture:)] autorelease];
    swipeGestureRight.direction = UISwipeGestureRecognizerDirectionRight;
    UISwipeGestureRecognizer* swipeGestureLeft = [[[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleGesture:)] autorelease];
    swipeGestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    self.imageView.userInteractionEnabled =YES;
    [self.imageView addGestureRecognizer:swipeGestureLeft];
    [self.imageView addGestureRecognizer:swipeGestureRight];
    
    self.start = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.start setTitle:@"立即体验" forState:UIControlStateNormal];
    [self.start setTitleColor:[UIColor redColor] forState:UIControlStateNormal];
    [self.start addTarget:self action:@selector(startAction) forControlEvents:UIControlEventTouchUpInside];
    self.start.frame = CGRectMake(SCREEN_WIDTH/2-50, SCREEN_HEIGHT/2, 100, 20);
    
    [self.view addSubview:self.imageView];
}

-(void)handleGesture:(UISwipeGestureRecognizer*)gestureRecognizer{
    UISwipeGestureRecognizerDirection direction =gestureRecognizer.direction;
    if(direction==UISwipeGestureRecognizerDirectionLeft){
        NSLog(@"left");
        if(self.index<GUIDE_IMAGE_COUNT-1){
            self.index++;
        }
    }
    if(direction==UISwipeGestureRecognizerDirectionRight){
        NSLog(@"right");
        if(self.index>0){
            self.index--;
        }
    }
    self.imageView.image =[UIImage imageNamed:@"首次进入提示"];
    if(self.start){
        [self.start removeFromSuperview];
    }
    if (self.index>=GUIDE_IMAGE_COUNT-1) {
        [self.view addSubview:self.start];
    }
}

-(void)startAction{
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    NSString* userName = [defaults objectForKey:LAST_USER_NAME];
    [defaults setValue:@"false" forKey:GUIDE_FLAG];
    //第一次打开应用直接显示登录页面
    if(userName==nil||userName.length==0){
        TTOpenURL(@"peacebird://login");
    }else{
        TTOpenURL(@"peacebird://index");
    }
}
@end
