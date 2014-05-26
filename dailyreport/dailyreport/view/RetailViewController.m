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
@implementation RetailViewController

@synthesize retailModel = _retailModel;
@synthesize pieChartView = _pieChartView;
@synthesize tabLables = _tabLables;
@synthesize channels = _channels;
@synthesize sorts = _sorts;
@synthesize regions = _regions;
@synthesize selectedData = _selectedData;
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
        
        self.pieChartView = [[PieChartView alloc] initWithFrame:CGRectMake(35,120,250,250)];
        self.pieChartView.delegate = self;
        self.pieChartView.datasource = self;
        self.tabLables = [NSMutableArray array];
        self.tableViews =[NSMutableArray array];
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
    
    self.channels = channels;
    self.regions = regions;
    self.sorts = sorts;
    
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
    [self updateTab];
    if([self.selectedData count]>0){
        [self.contentView addSubview:self.pieChartView];
        [self.pieChartView reloadData];
    }
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
        self.selectedData = self.channels;
    }else if([tabLabel.text isEqualToString:@"店铺形态"]){
        self.selectedData = self.sorts;
    }else if([tabLabel.text isEqualToString:@"管理形式"]){
        self.selectedData = self.regions;
    }
    [self updateTab];
}

-(void) updateTab{
    int index = 0;
    int sum = 0;
    [self.pieChartView reloadData];
    for(Retail* retail in self.selectedData){
        sum+=retail.dayAmount.intValue;
    }
    for(UIView* view in self.tableViews){
        [view removeFromSuperview];
    }
    [self.tableViews removeAllObjects];
    for (Retail* retail in self.selectedData){
        UIImageView* imageView =[self createImageViewFromColor:[StyleSheet getColorForIndex:index] frame:CGRectMake(10, 390+index*50, 20, 20)];
        [self.tableViews addObject:imageView];
        [self.contentView addSubview:imageView];
        
        UILabel* nameLabel =[self createLabel:retail.name frame:CGRectMake(40, 390+index*50, 120, 20) textColor:@"#5f5f5f" font:18 backgroundColor:nil];
        [self.tableViews addObject:nameLabel];
        [self.contentView addSubview:nameLabel];

        UILabel* percentLabel = [self createLabel:[NSString stringWithFormat:@"%0.1f%@",retail.dayAmount.intValue*1.0/sum*100,@"%"] frame:CGRectMake(170, 390+index*50, 70, 20) textColor:@"#5f5f5f" font:18 backgroundColor:nil];
        [self.tableViews addObject:percentLabel];
        [self.contentView addSubview:percentLabel];
        
        UILabel* amountLabel =[self createLabel:[NSString stringWithFormat:@"%i万元",retail.dayAmount.intValue/10000] frame:CGRectMake(240, 390+index*50, 80, 20) textColor:@"#5f5f5f" font:18 backgroundColor:nil];
        [self.tableViews addObject:amountLabel];
        [self.contentView addSubview:amountLabel];
        index++;
    }
    self.contentView.contentSize =
    CGSizeMake(320, 390+self.selectedData.count*50);
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
    return [self.selectedData count];
}
-(UIColor *)pieChartView:(PieChartView *)pieChartView colorForSliceAtIndex:(NSUInteger)index
{
    return [StyleSheet getColorForIndex:index];
}
-(int)pieChartView:(PieChartView *)pieChartView valueForSliceAtIndex:(NSUInteger)index
{
    Retail* retail = [self.selectedData objectAtIndex:index];
    return retail.dayAmount.intValue;
}

#pragma mark -


- (void)dealloc
{
    _retailModel.delegate = nil;
    TT_RELEASE_SAFELY(_retailModel);
    TT_RELEASE_SAFELY(_pieChartView);
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
