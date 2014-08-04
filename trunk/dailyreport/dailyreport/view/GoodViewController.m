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
#import "CRNavigator.h"

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

-(id) initWithBrand:(NSString*) brand index:(int)index{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.brand = brand;
        self.index=-1;
        self.dyviews = [NSMutableArray array];
        self.pointImageViews =[NSMutableArray array];
    }
    return self;
}

-(void) backAction{
    NSString* url = [NSString  stringWithFormat:@"peacebird://goodRank/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    TTOpenURL(url);

}

- (void)loadView
{
    [super loadView];
    self.contentView.goodSwitchDelegate = self;
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

    TTImageView* imageView = [[[TTImageView alloc] initWithFrame:CGRectMake(10, 44, 300, 430)] autorelease];
    imageView.urlPath = gr.imageLink;
    
    [self.contentView addSubview:imageView];
    [self.dyviews addObject:imageView];
    
    
    

    UIView* footView =[self createFootView:gr];
    [self.contentView addSubview:footView];
    [self.dyviews addObject:footView];
    self.contentView.contentSize =
    CGSizeMake(320, 474);
    if(IS_IPHONE5){
        self.contentView.scrollEnabled=NO;
    }
}

-(UIView*) createFootView:(GoodRank*) gr{
    
    
    UIView* footView = [[[UIView alloc] initWithFrame:CGRectMake(10, 474-80, 300, 80)] autorelease];
    NSLog(@"%f",self.contentView.frame.size.height);
    NSLog(@"%f",footView.frame.size.height);
    footView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.6f];
    UIView* view = nil;
    
    view =[self createLabel:[NSString stringWithFormat:@"排名: %i",DrAppDelegate.user.goodIndex+1] frame:CGRectMake(10, 0, 100, 20) textColor:@"#ffffff" font:16 backgroundColor:nil];
    [footView addSubview:view];

    
    view =[self createDecimalLabel:[NSNumber numberWithDouble:gr.amount.doubleValue/10000] unit:@"万元" frame:CGRectMake(120, 0, 60, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_CENTER];
    [footView addSubview:view];

    
    view =[self createDecimalLabel:[NSNumber numberWithInt:gr.count.intValue] unit:@"件" frame:CGRectMake(220, 0, 80, 20) textColor:@"#ffffff" font:16 backgroundColor:nil textAlignment:ALIGN_CENTER];
    [footView addSubview:view];
    
    view =[self createLabel:gr.name frame:CGRectMake(10, 20, 100, 20) textColor:@"#ffffff" font:16 backgroundColor:nil];
    [footView addSubview:view];
    
    view =[self createLabel:[NSString stringWithFormat:@"%@ %@ %@ %@",gr.year,gr.season,gr.line,gr.wave] frame:CGRectMake(10, 40, 200, 20) textColor:@"#ffffff" font:16 backgroundColor:nil];
    [footView addSubview:view];
    return footView;
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
    self.contentView.goodSwitchDelegate = nil;
    TT_RELEASE_SAFELY(_dyviews);
    TT_RELEASE_SAFELY(_pointImageViews);
    [super dealloc];
}

@end
