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

@synthesize webPieChartView = _webPieChartView;
@synthesize tabLables = _tabLables;
@synthesize tableViews = _tableViews;
@synthesize type =_type;
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
        self.type = @"channel";
        self.retailModel = [[[RetailModel alloc] initWithBrand:self.brand delegate:self] autorelease];
        self.tabLables = [NSMutableArray array];
        self.tableViews =[NSMutableArray array];
        self.index = 3;
    }
    return self;
}


-(void)handleGesture:(UISwipeGestureRecognizer*)gestureRecognizer{
    UISwipeGestureRecognizerDirection direction =gestureRecognizer.direction;
    NSString* url = nil;
    NSString* d = nil;
    if(direction==UISwipeGestureRecognizerDirectionLeft){
        NSLog(@"left");
       url = [NSString stringWithFormat:@"peacebird://storeRank/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
       d =@"left";
    }
    if(direction==UISwipeGestureRecognizerDirectionRight){
        NSLog(@"right");
        url = [NSString stringWithFormat:@"peacebird://kpi/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
        d =@"right";
    }
    
    TTOpenURL(url);
}
- (void)loadView
{
    [super loadView];
    UIView* dailyView = [self createDailyView:@"图标-小钱袋" label:@"零售额明细"];
    int channelFontSize = 20;
    int labelFontSize =18;
    NSLog(@"%f",SCREEN_WIDTH);
    if(SCREEN_WIDTH==320){
        channelFontSize = 20;
        labelFontSize =18;
    }else if(SCREEN_WIDTH==375){
        channelFontSize = 22;
        labelFontSize =20;
    }else{
        channelFontSize = 24;
        labelFontSize =22;
    }
    CGFloat width = SCREEN_WIDTH/3;
    int index = 0;
    CGFloat t = 0;
    NSMutableArray* tabs = [NSMutableArray array];
    [tabs addObject:@"店铺性质"];
    [tabs addObject:@"店铺形态"];
    [tabs addObject:@"管理部门"];
    for(NSString* tab in tabs){
        CGFloat x = index*width;
        CGFloat realWidth = width-0.5;
        if(index!=0){
            x =x +0.5;
        }
        if(index==[tabs count]-1){
            realWidth = SCREEN_WIDTH-t;
        }
        t+=width;
        UILabel* tabLabel = [self createLabel:tab frame:CGRectMake(x, dailyView.frame.size.height, realWidth, [self getTabHeight]) textColor:@"#636363" font:channelFontSize backgroundColor:@"#ffffff"];
        UITapGestureRecognizer* recognizer = [[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickChannelLabel:)] autorelease];
        tabLabel.userInteractionEnabled = YES;
        [tabLabel addGestureRecognizer:recognizer];
        tabLabel.textAlignment = [self getAlignment:ALIGN_CENTER];
        if(index!=0){
            tabLabel.textColor = [UIColor whiteColor];
            tabLabel.backgroundColor = [StyleSheet colorFromHexString:TAB_COLOR];
        }
        [self.contentView addSubview:tabLabel];
        [self.tabLables addObject:tabLabel];
        index++;
    }
    
    [self.contentView addSubview:dailyView];
    
    self.webPieChartView = [[[UIWebView alloc] initWithFrame:CGRectMake(0,SCREEN_HEIGHT*5/48+dailyView.frame.size.height,SCREEN_WIDTH,SCREEN_HEIGHT-SCREEN_HEIGHT*5/48-dailyView.frame.size.height)] autorelease];
    self.webPieChartView.scalesPageToFit=YES;
    self.webPieChartView.userInteractionEnabled =YES;
    CannotCancelUISwipeGestureRecognizer* swipeGestureRight = [[[CannotCancelUISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleGesture:)] autorelease];
    swipeGestureRight.direction = UISwipeGestureRecognizerDirectionRight;
    CannotCancelUISwipeGestureRecognizer* swipeGestureLeft = [[[CannotCancelUISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleGesture:)] autorelease];
    swipeGestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    [self.webPieChartView.scrollView addGestureRecognizer:swipeGestureLeft];
    [self.webPieChartView.scrollView addGestureRecognizer:swipeGestureRight];
    [self.retailModel load:self.type policy:TTURLRequestCachePolicyNone more:NO];
}

-(void)changeDateAndReload{
    [super changeDateAndReload];
    [self.retailModel load:self.type policy:TTURLRequestCachePolicyNone more:NO];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void) brandDidFinishLoad:(NSString*)dataProvider height:(int)height top:(int)top count:(int) count total:(long)total date:(NSDate*) date{
    NSLog(@"%@",@"加载Brand Retail数据成功");
    self.selectedDate = date;
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"MM月dd日 EEEE"];
    self.calendarLabel.text =[dateFormatter stringFromDate:date];
    
    
    [self.contentView addSubview:self.webPieChartView];
    NSString* htmlPath = [[NSBundle mainBundle] pathForResource:@"retail" ofType:@"html" inDirectory:@"web"];
    NSString* html = [NSString stringWithContentsOfFile:htmlPath encoding:NSUTF8StringEncoding error:nil];

    html=[html stringByReplacingOccurrencesOfString:@"$dataProvider" withString:dataProvider];
    
    html=[html stringByReplacingOccurrencesOfString:@"$top" withString:[NSString stringWithFormat:@"%i",top]];
    html=[html stringByReplacingOccurrencesOfString:@"$height" withString:[NSString stringWithFormat:@"%i",height]];
    html=[html stringByReplacingOccurrencesOfString:@"$total" withString:[NSString stringWithFormat:@"%li",total]];
    html=[html stringByReplacingOccurrencesOfString:@"$count" withString:[NSString stringWithFormat:@"%i",count]];
    NSLog(@"%@",html);
    NSLog(@"%@",[NSString stringWithFormat:@"%@/web/",[[NSBundle mainBundle] bundlePath]]);
    [self.webPieChartView loadHTMLString:html baseURL:[NSURL fileURLWithPath:[NSString stringWithFormat:@"%@/web/",[[NSBundle mainBundle] bundlePath]]]];
    self.contentView.contentSize =
    CGSizeMake(SCREEN_WIDTH, 390+count*30);
}

-(void)clickChannelLabel:(UIGestureRecognizer*)gestureRecognizer{
    UILabel* tabLabel = (UILabel*)gestureRecognizer.view;
    for(UILabel* l in self.tabLables){
        l.textColor = [UIColor whiteColor];
        l.backgroundColor = [StyleSheet colorFromHexString:TAB_COLOR];
    }
    tabLabel.textColor = [StyleSheet colorFromHexString:@"#636363"];
    tabLabel.backgroundColor =[StyleSheet colorFromHexString:@"#ffffff"];
    if([tabLabel.text isEqualToString:@"店铺性质"]){
        self.type = @"channel";
    }else if([tabLabel.text isEqualToString:@"店铺形态"]){
        self.type = @"sort";
    }else if([tabLabel.text isEqualToString:@"管理部门"]){
        self.type = @"region";
    }
    [self.retailModel load:self.type policy:TTURLRequestCachePolicyNone more:NO];
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
    TT_RELEASE_SAFELY(_tableViews);
    TT_RELEASE_SAFELY(_type);
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



@end
