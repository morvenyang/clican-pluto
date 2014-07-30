//
//  GoodViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-7-30.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "GoodViewController.h"
#import "AppDelegate.h"
#import "GoodRank.h"

@implementation GoodViewController
@synthesize dyviews = _dyviews;
@synthesize pointImageViews = _pointImageViews;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(id) initWithBrand:(NSString*) brand{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.brand = brand;
        self.index=-1;
        self.dyviews = [NSMutableArray array];
        self.pointImageViews =[NSMutableArray array];
    }
    return self;
}

-(void) backAction{
    NSString* url = [NSString stringWithFormat:@"peacebird://goodRank/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    TTOpenURL(url);
}

- (void)loadView
{
    [super loadView];
    UIView* dailyView = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 34)] autorelease];
    NSString* imageName = [NSString stringWithFormat:@"每日收入%@背景.png",self.brand];
    dailyView.backgroundColor =[UIColor colorWithPatternImage:[UIImage imageNamed:imageName]];
    
    UIImageView* retailImageView = [self createImageViewFromNamedImage:@"图标-零售收入.png" frame:CGRectMake(0, 0, 34, 34)];
    
    
    UILabel* retailLabel = [self createLabel:@"商品信息" frame:CGRectMake(40, 0, 120, 34) textColor:@"#ffffff" font:12 backgroundColor:nil];
    
    
    UIButton* calendarButton = [UIButton buttonWithType:UIButtonTypeCustom];
    calendarButton.frame =CGRectMake(160, 0, 34, 34);
    [calendarButton setImage:[UIImage imageNamed:@"图标-日历.png"] forState:UIControlStateNormal];
    [calendarButton addTarget:self action:@selector(openCalendar:) forControlEvents:UIControlEventTouchUpInside];
    
    NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:@"MM月dd日 EEEE"];
    
    UILabel* calendarLabel = [self createLabel:[dateFormatter stringFromDate:DrAppDelegate.user.goodDate] frame:CGRectMake(200, 0, 120, 34) textColor:@"#ffffff" font:12 backgroundColor:nil];
    
    [dailyView addSubview:retailImageView];
    [dailyView addSubview:retailLabel];
    //[dailyView addSubview:calendarButton];
    [dailyView addSubview:calendarLabel];
    
    
    [self.contentView addSubview:dailyView];
    [self switchGood];
}

-(void)switchGood{
    for(UIView* view in self.dyviews){
        [view removeFromSuperview];
    }
    int i = 0;
    UIImage* lightImage= [UIImage imageNamed:@"图标-分页原点-正常.png"];
    UIImage* highLightImage= [UIImage imageNamed:@"图标-分页原点-高亮.png"];
    for(UIImageView* view in self.pointImageViews){
        if(i==DrAppDelegate.user.goodIndex){
            view.image =highLightImage;
        }else{
            view.image =lightImage;
        }
        i++;
        
    }
    GoodRank* gr = [DrAppDelegate.user.goods objectAtIndex:DrAppDelegate.user.goodIndex];
    UIView* view = nil;
    view =[self createLabel:[NSString stringWithFormat:@"品名: %@",gr.name] frame:CGRectMake(10, 36, 150, 20) textColor:@"#000000" font:12 backgroundColor:nil];
    [self.contentView addSubview:view];
    [self.dyviews addObject:view];
    view =[self createLabel:[NSString stringWithFormat:@"排行: %i",DrAppDelegate.user.goodIndex+1] frame:CGRectMake(260, 36, 40, 20) textColor:@"#000000" font:12 backgroundColor:nil];
    [self.contentView addSubview:view];
    [self.dyviews addObject:view];
    
    TTImageView* imageView = [[[TTImageView alloc] initWithFrame:CGRectMake(20, 60, 280, 280)] autorelease];
    imageView.urlPath = gr.imageLink;
    
    [self.contentView addSubview:imageView];
    [self.dyviews addObject:imageView];
    
    view =[self createLabel:@"商品信息" frame:CGRectMake(10, 340, 150, 20) textColor:@"#000000" font:12 backgroundColor:nil];
    [self.contentView addSubview:view];
    [self.dyviews addObject:view];
    
    view =[self createLabel:@"系列" frame:CGRectMake(20, 360, 50, 20) textColor:@"#000000" font:15 backgroundColor:nil];
    [self.contentView addSubview:view];
    [self.dyviews addObject:view];
    
    view =[self createLabel:gr.line frame:CGRectMake(220, 360,100, 20) textColor:@"#000000" font:15 backgroundColor:nil];
    [self.contentView addSubview:view];
    [self.dyviews addObject:view];
    
    view =[self createLabel:@"年份" frame:CGRectMake(20, 380, 100, 20) textColor:@"#000000" font:15 backgroundColor:nil];
    [self.contentView addSubview:view];
    [self.dyviews addObject:view];
    
    view =[self createLabel:gr.year frame:CGRectMake(220, 380, 100, 20) textColor:@"#000000" font:15 backgroundColor:nil];
    [self.contentView addSubview:view];
    [self.dyviews addObject:view];
    
    view =[self createLabel:@"季节" frame:CGRectMake(20, 400, 100, 20) textColor:@"#000000" font:15 backgroundColor:nil];
    [self.contentView addSubview:view];
    [self.dyviews addObject:view];
    
    view =[self createLabel:gr.season frame:CGRectMake(220, 400, 100, 20) textColor:@"#000000" font:15 backgroundColor:nil];
    [self.contentView addSubview:view];
    [self.dyviews addObject:view];
    
    view =[self createLabel:@"波段" frame:CGRectMake(20, 420, 100, 20) textColor:@"#000000" font:15 backgroundColor:nil];
    [self.contentView addSubview:view];
    [self.dyviews addObject:view];
    
    view =[self createLabel:gr.wave frame:CGRectMake(220, 420, 100, 20) textColor:@"#000000" font:15 backgroundColor:nil];
    [self.contentView addSubview:view];
    [self.dyviews addObject:view];
    
    view =[self createLabel:@"件数" frame:CGRectMake(20, 440, 100, 20) textColor:@"#000000" font:15 backgroundColor:nil];
    [self.contentView addSubview:view];
    [self.dyviews addObject:view];
    
    view =[self createLabel:[NSString stringWithFormat:@"%i",gr.count.intValue] frame:CGRectMake(220, 440, 100, 20) textColor:@"#000000" font:15 backgroundColor:nil];
    [self.contentView addSubview:view];
    [self.dyviews addObject:view];
    self.contentView.contentSize =
    CGSizeMake(320, 460);
}

-(UIView*) createPaginationView:(int)y{
    UIView* paginationView = [[[UIView alloc] initWithFrame:CGRectMake((320-16*DrAppDelegate.user.goods.count)/2, y, 16*DrAppDelegate.user.goods.count, 30)] autorelease];
    UIImage* lightImage= [UIImage imageNamed:@"图标-分页原点-正常.png"];
    UIImage* highLightImage= [UIImage imageNamed:@"图标-分页原点-高亮.png"];
    paginationView.backgroundColor = [UIColor whiteColor];
    for(int i=1;i<=DrAppDelegate.user.goods.count;i++){
        UIImageView* v = [[[UIImageView alloc] initWithFrame:CGRectMake(16*(i-1), 10, 6, 6)] autorelease];
        if(i==DrAppDelegate.user.goodIndex){
            v.image = highLightImage;
        }else{
            v.image = lightImage;
        }
        [self.pointImageViews addObject:v];
        [paginationView addSubview:v];
    }
    return paginationView;
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc
{

    TT_RELEASE_SAFELY(_dyviews);
    [super dealloc];
}

@end
