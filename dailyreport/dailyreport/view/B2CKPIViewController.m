//
//  B2CKPIViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-7-3.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "B2CKPIViewController.h"
#import "AppDelegate.h"


@implementation B2CKPIViewController
@synthesize kpiModel = _kpiModel;

-(id) initWithBrand:(NSString*) brand{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.brand = brand;
        self.kpiModel = [[[KPIModel alloc] initWithBrand:self.brand delegate:self] autorelease];
        self.index = 8;
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
    UIView* dailyView = [self createDailyView:@"图标-关键指标" label:@"电商指标"];
    
    Channel* channel = nil;
    for (Channel* c in channels ) {
        if([c.channel isEqualToString:@"电商"]){
            channel = c;
            break;
        }
    }
    [self.contentView addSubview:dailyView];
//    if(channel==nil){
//        TTAlert(@"没有相关电商指标数据");
//        return;
//    }
    
    CGFloat hOffset = dailyView.frame.size.height;
    int labelFontSize =[self getFont:18 ip6Offset:2 ip6pOffset:4];
    UIImage* labelImage = [UIImage imageNamed:@"零售收入"];
    CGFloat rowImageHeight = labelImage.size.height;
    CGFloat labelOffset = 50;
    if(IS_IPHONE6){
        rowImageHeight = rowImageHeight*1.2;
    }
    if(IS_IPHONE6_PLUS){
        labelOffset = 70;
    }
    CGFloat rowHeight = rowImageHeight*2;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"零售收入" point:CGPointMake(20,hOffset+rowImageHeight/2)]];
    [self.contentView addSubview:[self createLabel:@"零售额" frame:CGRectMake(labelOffset,hOffset,100,rowHeight) textColor:@"#4a4a4a" font:labelFontSize backgroundColor:nil]];
    
    [self.contentView addSubview:[self createDecimalLabel:[NSNumber numberWithDouble:channel.dayAmount.doubleValue/10000] unit:@"万元" frame:CGRectMake(SCREEN_WIDTH*11/16,hOffset,SCREEN_WIDTH*5/16,rowHeight) textColor:@"#7f7f7f" font:labelFontSize backgroundColor:nil textAlignment:ALIGN_LEFT]];
    hOffset+=rowHeight;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线" frame:CGRectMake(0,hOffset,SCREEN_WIDTH,3)]];
    hOffset+=3;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"票数" point:CGPointMake(20,hOffset+rowImageHeight/2)]];
    [self.contentView addSubview:[self createLabel:@"票数" frame:CGRectMake(labelOffset,hOffset,100,rowHeight) textColor:@"#4a4a4a" font:labelFontSize backgroundColor:nil]];

    [self.contentView addSubview:[self createDecimalLabel:[NSNumber numberWithInt:channel.docNumber.intValue] frame:CGRectMake(SCREEN_WIDTH*11/16,hOffset,SCREEN_WIDTH*5/16,rowHeight) textColor:@"#7f7f7f" font:labelFontSize backgroundColor:nil textAlignment:ALIGN_LEFT]];
    hOffset+=rowHeight;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线" frame:CGRectMake(0,hOffset,SCREEN_WIDTH,3)]];
    hOffset+=3;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"附加" point:CGPointMake(20,hOffset+rowImageHeight/2)]];
    [self.contentView addSubview:[self createLabel:@"附加" frame:CGRectMake(labelOffset,hOffset,100,rowHeight)  textColor:@"#4a4a4a" font:labelFontSize backgroundColor:nil]];
    [self.contentView addSubview:[self createLabel:[NSString stringWithFormat:@"%0.1f",channel.avgDocCount.doubleValue] frame:CGRectMake(SCREEN_WIDTH*11/16,hOffset,SCREEN_WIDTH*5/16,rowHeight) textColor:@"#7f7f7f" font:labelFontSize backgroundColor:nil textAlignment:ALIGN_LEFT]];
    hOffset+=rowHeight;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线" frame:CGRectMake(0,hOffset,SCREEN_WIDTH,3)]];
    hOffset+=3;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"件单价" point:CGPointMake(20,hOffset+rowImageHeight/2)]];
    [self.contentView addSubview:[self createLabel:@"件单价" frame:CGRectMake(labelOffset,hOffset,100,rowHeight) textColor:@"#4a4a4a" font:labelFontSize backgroundColor:nil]];

    [self.contentView addSubview:[self createDecimalLabel:[NSNumber numberWithInt:channel.avgPrice.intValue] unit:@"元" frame:CGRectMake(SCREEN_WIDTH*11/16,hOffset,SCREEN_WIDTH*5/16,rowHeight) textColor:@"#7f7f7f" font:18 backgroundColor:nil textAlignment:ALIGN_LEFT]];
    hOffset+=rowHeight;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线" frame:CGRectMake(0,hOffset,SCREEN_WIDTH,3)]];
    hOffset+=3;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"单效" point:CGPointMake(20,hOffset+rowImageHeight/2)]];
    [self.contentView addSubview:[self createLabel:@"坪效" frame:CGRectMake(labelOffset,hOffset,100,rowHeight)  textColor:@"#4a4a4a" font:labelFontSize backgroundColor:nil]];

    [self.contentView addSubview:[self createDecimalLabel:[NSNumber numberWithInt:channel.aps.intValue] unit:@"元" frame:CGRectMake(SCREEN_WIDTH*11/16,hOffset,SCREEN_WIDTH*5/16,rowHeight)textColor:@"#7f7f7f" font:labelFontSize backgroundColor:nil textAlignment:ALIGN_LEFT]];
    hOffset+=rowHeight;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-背景花纹" frame:CGRectMake(0,hOffset,SCREEN_WIDTH,7)]];
    hOffset+=7;

    
    self.contentView.contentSize =
    CGSizeMake(SCREEN_WIDTH, hOffset);
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
