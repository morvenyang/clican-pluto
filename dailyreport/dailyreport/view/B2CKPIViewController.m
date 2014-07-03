//
//  B2CKPIViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-7-3.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "B2CKPIViewController.h"



@implementation B2CKPIViewController
@synthesize kpiModel = _kpiModel;

-(id) initWithBrand:(NSString*) brand{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.brand = brand;
        self.kpiModel = [[[KPIModel alloc] initWithBrand:self.brand delegate:self] autorelease];
        self.index = 5;
    }
    return self;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)loadView
{
    [super loadView];
    [self.kpiModel load:TTURLRequestCachePolicyNone more:NO];
}

-(void)changeDateAndReload{
    [super changeDateAndReload];
    [self.kpiModel load:TTURLRequestCachePolicyNone more:NO];
}

- (void) brandDidFinishLoad:(NSArray*) channels date:(NSDate*) date{
    NSLog(@"%@",@"加载Brand KPI数据成功");
    self.selectedDate = date;
    UIView* dailyView = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 34)] autorelease];
    NSString* imageName = [NSString stringWithFormat:@"每日收入%@背景.png",self.brand];
    dailyView.backgroundColor =[UIColor colorWithPatternImage:[UIImage imageNamed:imageName]];
    
    UIImageView* retailImageView = [self createImageViewFromNamedImage:@"图标-关键指标.png" frame:CGRectMake(0, 0, 34, 34)];
    
    
    UILabel* retailLabel = [self createLabel:@"电商指标" frame:CGRectMake(40, 0, 120, 34) textColor:@"#ffffff" font:12 backgroundColor:nil];
    
    
    UIButton* calendarButton = [UIButton buttonWithType:UIButtonTypeCustom];
    calendarButton.frame =CGRectMake(160, 0, 34, 34);
    [calendarButton setImage:[UIImage imageNamed:@"图标-日历.png"] forState:UIControlStateNormal];
    [calendarButton addTarget:self action:@selector(openCalendar:) forControlEvents:UIControlEventTouchUpInside];
    
    NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:@"MM月dd日 EEEE"];
    
    UILabel* calendarLabel = [self createLabel:[dateFormatter stringFromDate:date] frame:CGRectMake(200, 0, 120, 34) textColor:@"#ffffff" font:12 backgroundColor:nil];
    
    [dailyView addSubview:retailImageView];
    [dailyView addSubview:retailLabel];
    //[dailyView addSubview:calendarButton];
    [dailyView addSubview:calendarLabel];
    
    Channel* channel = nil;
    for (Channel* c in channels ) {
        if([c.channel isEqualToString:@"电商"]){
            channel = c;
            break;
        }
    }
    [self.contentView addSubview:dailyView];
    if(channel==nil){
        TTAlert(@"没有相关电商指标数据");
        return;
    }
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"零售收入.png" frame:CGRectMake(20,64,24,24)]];
    [self.contentView addSubview:[self createLabel:@"零售额" frame:CGRectMake(50,50,100,49) textColor:@"#4a4a4a" font:18 backgroundColor:nil]];
    UILabel* dayAmountLabel =[self createDecimalLabel:[NSNumber numberWithInt:channel.dayAmount.intValue/10000] unit:@"万元" frame:CGRectMake(200,50,100,49) textColor:@"#7f7f7f" font:18 backgroundColor:nil textAlignment:ALIGN_LEFT];
    
    [self.contentView addSubview:dayAmountLabel];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线.png" frame:CGRectMake(0,107,320,3)]];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"票数.png" frame:CGRectMake(20,124,24,24)]];
    [self.contentView addSubview:[self createLabel:@"票数" frame:CGRectMake(50,110,100,49) textColor:@"#4a4a4a" font:18 backgroundColor:nil]];
    UILabel* docNumberLabel =[self createDecimalLabel:[NSNumber numberWithInt:channel.docNumber.intValue] frame:CGRectMake(200,110,100,49) textColor:@"#7f7f7f" font:18 backgroundColor:nil textAlignment:ALIGN_LEFT];
    [self.contentView addSubview:docNumberLabel];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线.png" frame:CGRectMake(0,167,320,3)]];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"附加.png" frame:CGRectMake(20,184,24,24)]];
    [self.contentView addSubview:[self createLabel:@"附加" frame:CGRectMake(50,170,100,49) textColor:@"#4a4a4a" font:18 backgroundColor:nil]];
    UILabel* avgDocCountLabel =[self createLabel:[NSString stringWithFormat:@"%0.1f",channel.avgDocCount.doubleValue] frame:CGRectMake(200,170,100,49) textColor:@"#7f7f7f" font:18 backgroundColor:nil textAlignment:ALIGN_LEFT];
    [self.contentView addSubview:avgDocCountLabel];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线.png" frame:CGRectMake(0,227,320,3)]];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"件单价.png" frame:CGRectMake(20,244,24,24)]];
    [self.contentView addSubview:[self createLabel:@"件单价" frame:CGRectMake(50,230,100,49) textColor:@"#4a4a4a" font:18 backgroundColor:nil]];
    UILabel* avgPriceLabel =[self createDecimalLabel:[NSNumber numberWithInt:channel.avgPrice.intValue] unit:@"元" frame:CGRectMake(200,230,100,49) textColor:@"#7f7f7f" font:18 backgroundColor:nil textAlignment:ALIGN_LEFT];
    [self.contentView addSubview:avgPriceLabel];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线.png" frame:CGRectMake(0,287,320,3)]];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"单效.png" frame:CGRectMake(20,306,24,24)]];
    [self.contentView addSubview:[self createLabel:@"坪效" frame:CGRectMake(50,290,100,49) textColor:@"#4a4a4a" font:18 backgroundColor:nil]];
    UILabel* apsLabel =[self createDecimalLabel:[NSNumber numberWithInt:channel.aps.intValue] unit:@"元" frame:CGRectMake(200,290,100,49) textColor:@"#7f7f7f" font:18 backgroundColor:nil textAlignment:ALIGN_LEFT];
    [self.contentView addSubview:apsLabel];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-背景花纹.png" frame:CGRectMake(0,347,320,7)]];
    
    
    self.contentView.contentSize =
    CGSizeMake(320, 404);
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
    _kpiModel.delegate = nil;
    TT_RELEASE_SAFELY(_kpiModel);
    [super dealloc];
}

@end
