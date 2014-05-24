//
//  RetailViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-5-21.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "RetailViewController.h"

@implementation RetailViewController

@synthesize retailModel = _retailModel;

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
        
        self.pieChartView = [[PieChartView alloc] initWithFrame:CGRectMake(35,70,250,250)];
        self.pieChartView.delegate = self;
        self.pieChartView.datasource = self;
        
        self.index = 3;
    }
    return self;
}

- (void)loadView
{
    [super loadView];
    [self.retailModel load:TTURLRequestCachePolicyNone more:NO];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void) brandDidFinishLoad:(NSArray*) channels sorts:(NSArray*) sorts regions:(NSArray*) regions date:(NSDate*) date{
    NSLog(@"%@",@"加载Brand Retail数据成功");
    
    UIView* dailyView = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 34)] autorelease];
    NSString* imageName = [NSString stringWithFormat:@"每日收入%@背景.png",self.brand];
    dailyView.backgroundColor =[UIColor colorWithPatternImage:[UIImage imageNamed:imageName]];
    
    UIImageView* retailImageView = [self createImageViewFromNamedImage:@"图标-零售收入.png" frame:CGRectMake(0, 0, 34, 34)];
    
    
    UILabel* retailLabel = [self createLabel:@"零售收入明细" frame:CGRectMake(40, 0, 120, 34) textColor:@"#ffffff" font:12 backgroundColor:nil];
    
    
    UIImageView* calendarImageView = [self createImageViewFromNamedImage:@"图标-日历.png" frame:CGRectMake(160, 0, 34, 34)];
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"MM月dd日 EEEE"];
    
    UILabel* calendarLabel = [self createLabel:[dateFormatter stringFromDate:date] frame:CGRectMake(200, 0, 120, 34) textColor:@"#ffffff" font:12 backgroundColor:nil];
    
    [dailyView addSubview:retailImageView];
    [dailyView addSubview:retailLabel];
    [dailyView addSubview:calendarImageView];
    [dailyView addSubview:calendarLabel];
    

    
    
    [self.contentView addSubview:dailyView];
    [self.contentView addSubview:self.pieChartView];
    [self.pieChartView reloadData];
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

#pragma mark -    PieChartViewDelegate
-(CGFloat)centerCircleRadius
{
    return 75.0;
}
#pragma mark - PieChartViewDataSource
-(int)numberOfSlicesInPieChartView:(PieChartView *)pieChartView
{
    return 6;
}
-(UIColor *)pieChartView:(PieChartView *)pieChartView colorForSliceAtIndex:(NSUInteger)index
{
    return GetRandomUIColor();
}
-(int)pieChartView:(PieChartView *)pieChartView valueForSliceAtIndex:(NSUInteger)index
{
    return 50;
}
-(int) totalAmount{
    return 100;
}
#pragma mark -

static inline UIColor *GetRandomUIColor()
{
    CGFloat r = arc4random() % 255;
    CGFloat g = arc4random() % 255;
    CGFloat b = arc4random() % 255;
    UIColor * color = [UIColor colorWithRed:r/255 green:g/255 blue:b/255 alpha:1.0f];
    return color;
}
- (void)dealloc
{
    _retailModel.delegate = nil;
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



@end
