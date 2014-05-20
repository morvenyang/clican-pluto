//
//  SwitchViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-5-20.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "SwitchViewController.h"



@implementation SwitchViewController

@synthesize contentView = _contentView;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
-(void)handleSwipeFrom:(UISwipeGestureRecognizer*)recognize{
    NSLog(@"switch left or right");
}

-(void)addCommonViewFromIndex:(int)index{
    UIImage* lightImage= [UIImage imageNamed:@"图标-分页原点-正常.png"];
    UIImage* highLightImage= [UIImage imageNamed:@"图标-分页原点-高亮.png"];
    CGRect frame = [[UIScreen mainScreen] bounds];
    int offset = 0;
    if(DEVICE_VERSION<7.0){
        offset = 64;
    }
    CGFloat y = frame.size.height-30-offset;
    UIView* paginationView = [[[UIView alloc] initWithFrame:CGRectMake(130, y, 60, 30)] autorelease];
    paginationView.backgroundColor = [UIColor whiteColor];
    for(int i=1;i<=4;i++){
        UIImageView* v = [[[UIImageView alloc] initWithFrame:CGRectMake(15*(i-1), 10, 6, 6)] autorelease];
        if(i==index){
            v.image = highLightImage;
        }else{
            v.image = lightImage;
        }
        [paginationView addSubview:v];
    }
    [self.view addSubview:paginationView];
}

- (void)loadView
{
    
    [super loadView];
    CGRect frame = [[UIScreen mainScreen] bounds];
    NSLog(@"%f",frame.size.height);
    self.view.backgroundColor = [UIColor whiteColor];
    int offset = 0;
    if(DEVICE_VERSION<7.0){
        offset= 64;
    }
    self.contentView = [[[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height-offset-30)] autorelease];
    
    self.contentView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.contentView];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    UISwipeGestureRecognizer* recognizer;
    recognizer = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipeFrom:)];
    
    [recognizer setDirection:(UISwipeGestureRecognizerDirectionLeft)];
    [[self view] addGestureRecognizer:recognizer];
    [recognizer release];
    
    recognizer = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleSwipeFrom:)];
    
    [recognizer setDirection:(UISwipeGestureRecognizerDirectionRight)];
    [[self view] addGestureRecognizer:recognizer];
    [recognizer release];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
