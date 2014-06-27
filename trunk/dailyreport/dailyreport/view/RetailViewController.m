//
//  RetailViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-5-21.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "RetailViewController.h"
#import "StyleSheet.h"
#import "Retail.h"
#import "CannotCancelUISwipeGestureRecognizer.h"
#import "AppDelegate.h"
@implementation RetailViewController

@synthesize retailModel = _retailModel;
@synthesize webPieChartView = _webPieChartView;
@synthesize selectedData = _selectedData;
@synthesize tabLables = _tabLables;
@synthesize channels = _channels;
@synthesize sorts = _sorts;
@synthesize regions = _regions;
@synthesize tableViews = _tableViews;
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
        self.retailModel = [[[RetailModel alloc] initWithBrand:self.brand delegate:self] autorelease];
        
        self.webPieChartView = [[UIWebView alloc] initWithFrame:CGRectMake(0,80,320,400)];
        self.webPieChartView.scalesPageToFit=YES;
        self.webPieChartView.userInteractionEnabled =YES;
        CannotCancelUISwipeGestureRecognizer* swipeGestureRight = [[[CannotCancelUISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleGesture:)] autorelease];
        swipeGestureRight.direction = UISwipeGestureRecognizerDirectionRight;
        CannotCancelUISwipeGestureRecognizer* swipeGestureLeft = [[[CannotCancelUISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleGesture:)] autorelease];
        swipeGestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
        [self.webPieChartView.scrollView addGestureRecognizer:swipeGestureLeft];
        [self.webPieChartView.scrollView addGestureRecognizer:swipeGestureRight];
        self.tabLables = [NSMutableArray array];
        self.tableViews =[NSMutableArray array];
        self.index = 3;
    }
    return self;
}


-(void)handleGesture:(UISwipeGestureRecognizer*)gestureRecognizer{
    UISwipeGestureRecognizerDirection direction =gestureRecognizer.direction;
    if(direction==UISwipeGestureRecognizerDirectionLeft){
        NSLog(@"left");
        if(self.index==1){
            NSString* url = [NSString stringWithFormat:@"peacebird://kpi/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            TTOpenURL(url);
        }else if(self.index==2){
            NSString* url = [NSString stringWithFormat:@"peacebird://retail/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            TTOpenURL(url);
        }else if(self.index==3){
            NSString* url = [NSString stringWithFormat:@"peacebird://storeRank/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            TTOpenURL(url);
        }
        return;
    }
    if(direction==UISwipeGestureRecognizerDirectionRight){
        NSLog(@"right");
        if(self.index==2){
            NSString* url = [NSString stringWithFormat:@"peacebird://brand/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            TTOpenURL(url);
        }else if(self.index==3){
            NSString* url = [NSString stringWithFormat:@"peacebird://kpi/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            TTOpenURL(url);
        }else if(self.index==4){
            NSString* url = [NSString stringWithFormat:@"peacebird://retail/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            TTOpenURL(url);
        }
        return;
    }
}
- (void)loadView
{
    [super loadView];
    [self.retailModel load:TTURLRequestCachePolicyNone more:NO];
}

-(void)changeDateAndReload{
    [super changeDateAndReload];
    [self.retailModel load:TTURLRequestCachePolicyNone more:NO];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void) brandDidFinishLoad:(NSArray*) channels sorts:(NSArray*) sorts regions:(NSArray*) regions date:(NSDate*) date{
    NSLog(@"%@",@"加载Brand Retail数据成功");
    self.selectedDate = date;
    self.channels = channels;
    self.regions = regions;
    self.sorts = sorts;
    
    UIView* dailyView = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 34)] autorelease];
    NSString* imageName = [NSString stringWithFormat:@"每日收入%@背景.png",self.brand];
    dailyView.backgroundColor =[UIColor colorWithPatternImage:[UIImage imageNamed:imageName]];
    
    UIImageView* retailImageView = [self createImageViewFromNamedImage:@"图标-小钱袋.png" frame:CGRectMake(0, 0, 34, 34)];
    
    
    UILabel* retailLabel = [self createLabel:@"零售额明细" frame:CGRectMake(40, 0, 120, 34) textColor:@"#ffffff" font:12 backgroundColor:nil];
    
    
    UIButton* calendarButton = [UIButton buttonWithType:UIButtonTypeCustom];
    calendarButton.frame =CGRectMake(160, 0, 34, 34);
    [calendarButton setImage:[UIImage imageNamed:@"图标-日历.png"] forState:UIControlStateNormal];
    [calendarButton addTarget:self action:@selector(openCalendar:) forControlEvents:UIControlEventTouchUpInside];
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"MM月dd日 EEEE"];
    
    UILabel* calendarLabel = [self createLabel:[dateFormatter stringFromDate:date] frame:CGRectMake(200, 0, 120, 34) textColor:@"#ffffff" font:12 backgroundColor:nil];
    
    [dailyView addSubview:retailImageView];
    [dailyView addSubview:retailLabel];
    //[dailyView addSubview:calendarButton];
    [dailyView addSubview:calendarLabel];
    
    CGFloat width = 320.0/3;
    int index = 0;
    NSMutableArray* tabs = [NSMutableArray array];
    [tabs addObject:@"店铺性质"];
    [tabs addObject:@"店铺形态"];
    [tabs addObject:@"管理形式"];
    for(NSString* tab in tabs){
        UILabel* tabLabel = [self createLabel:tab frame:CGRectMake(0+index*width, 34, width, 50) textColor:@"#636363" font:20 backgroundColor:@"#ffffff"];
        UITapGestureRecognizer* recognizer = [[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickChannelLabel:)] autorelease];
        tabLabel.userInteractionEnabled = YES;
        [tabLabel addGestureRecognizer:recognizer];
        tabLabel.textAlignment = NSTextAlignmentCenter;
        if(index!=0){
            tabLabel.textColor = [UIColor whiteColor];
            tabLabel.backgroundColor = [StyleSheet colorFromHexString:@"#bdbdbd"];
        }
        [self.contentView addSubview:tabLabel];
        [self.tabLables addObject:tabLabel];
        index++;
    }
    
    [self.contentView addSubview:dailyView];
    
    self.selectedData = self.channels;
    [self updateTab:@"channel"];
}

-(void)clickChannelLabel:(UIGestureRecognizer*)gestureRecognizer{
    UILabel* tabLabel = (UILabel*)gestureRecognizer.view;
    for(UILabel* l in self.tabLables){
        l.textColor = [UIColor whiteColor];
        l.backgroundColor = [StyleSheet colorFromHexString:@"#bdbdbd"];
    }
    tabLabel.textColor = [StyleSheet colorFromHexString:@"#636363"];
    tabLabel.backgroundColor =[StyleSheet colorFromHexString:@"#ffffff"];
    if([tabLabel.text isEqualToString:@"店铺性质"]){
        [self updateTab:@"channel"];
        self.selectedData = self.channels;
    }else if([tabLabel.text isEqualToString:@"店铺形态"]){
        [self updateTab:@"sort"];
        self.selectedData = self.sorts;
    }else if([tabLabel.text isEqualToString:@"管理形式"]){
        [self updateTab:@"region"];
        self.selectedData = self.regions;
    }
    
}

-(void) updateTab:(NSString*) type{
    [self.contentView addSubview:self.webPieChartView];
    NSString* url =[NSString stringWithFormat:@"%@/retailChart.do?brand=%@&type=%@",BASE_URL,[self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding],type];
    if(DrAppDelegate.user.date!=nil){
        NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
        [dateFormatter setTimeStyle:NSDateFormatterFullStyle];
        [dateFormatter setDateFormat:@"yyyyMMdd"];
        NSString* strDate = [dateFormatter stringFromDate:DrAppDelegate.user.date];
        url = [url stringByAppendingFormat:@"&date=%@",strDate];
    }
    
    [self.webPieChartView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]]];
    self.contentView.contentSize =
    CGSizeMake(320, 390+self.selectedData.count*30);
}
- (void) brandDidStartLoad:(NSString*) brand{
    NSLog(@"%@",@"开始加载Brand数据");
}

- (void) brand:(NSString*)brand didFailLoadWithError:(NSError*)error{
    if([error code]==-1004||[error code]==-1001){
        TTAlert(@"请检查网络链接");
    }else{
        TTAlert([error localizedDescription]);
    }
}



- (void)dealloc
{
    _retailModel.delegate = nil;
    TT_RELEASE_SAFELY(_retailModel);
    TT_RELEASE_SAFELY(_webPieChartView);
    TT_RELEASE_SAFELY(_tabLables);
    TT_RELEASE_SAFELY(_channels);
    TT_RELEASE_SAFELY(_sorts);
    TT_RELEASE_SAFELY(_regions);
    TT_RELEASE_SAFELY(_tableViews);
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



@end
