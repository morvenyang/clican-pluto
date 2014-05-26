//
//  KPIViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-5-20.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "KPIViewController.h"
#import "StyleSheet.h"
@implementation KPIViewController
@synthesize kpiModel = _kpiModel;
@synthesize channels = _channels;
@synthesize dayAmountLabel = _dayAmountLabel;
@synthesize docNumberLabel = _docNumberLabel;
@synthesize avgDocCountLabel=_avgDocCountLabel;
@synthesize avgPriceLabel = _avgPriceLabel;
@synthesize apsLabel = _apsLabel;
@synthesize channelLables = _channelLables;
-(id) initWithBrand:(NSString*) brand{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.brand = brand;
        self.kpiModel = [[[KPIModel alloc] initWithBrand:self.brand delegate:self] autorelease];
        self.channelLables = [NSMutableArray array];
        self.index = 2;
    }
    return self;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
       
    }
    return self;
}

- (void)loadView
{
    [super loadView];
    [self.kpiModel load:TTURLRequestCachePolicyNone more:NO];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}
-(void)clickChannelLabel:(UIGestureRecognizer*)gestureRecognizer{
    UILabel* channelLabel = (UILabel*)gestureRecognizer.view;
    Channel* channel = nil;
    for(UILabel* l in self.channelLables){
        l.textColor = [UIColor whiteColor];
        l.backgroundColor = [UIColor blackColor];
    }
    channelLabel.textColor = [StyleSheet colorFromHexString:@"#636363"];
    channelLabel.backgroundColor =[StyleSheet colorFromHexString:@"#ffffff"];
    for(Channel* c in self.channels){
        if([c.channel isEqualToString:channelLabel.text]){
            channel = c;
            break;
        }
    }
    
    [self updateChannel:channel];
}
- (void) brandDidFinishLoad:(NSArray*) channels date:(NSDate*) date{
    NSLog(@"%@",@"加载Brand KPI数据成功");
    self.channels = channels;
    UIView* dailyView = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 34)] autorelease];
    NSString* imageName = [NSString stringWithFormat:@"每日收入%@背景.png",self.brand];
    dailyView.backgroundColor =[UIColor colorWithPatternImage:[UIImage imageNamed:imageName]];

    UIImageView* retailImageView = [self createImageViewFromNamedImage:@"图标-关键指标.png" frame:CGRectMake(0, 0, 34, 34)];

    
    UILabel* retailLabel = [self createLabel:@"关键指标" frame:CGRectMake(40, 0, 120, 34) textColor:@"#ffffff" font:12 backgroundColor:nil];
    

    UIImageView* calendarImageView = [self createImageViewFromNamedImage:@"图标-日历.png" frame:CGRectMake(160, 0, 34, 34)];
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"MM月dd日 EEEE"];
    
    UILabel* calendarLabel = [self createLabel:[dateFormatter stringFromDate:date] frame:CGRectMake(200, 0, 120, 34) textColor:@"#ffffff" font:12 backgroundColor:nil];
    
    [dailyView addSubview:retailImageView];
    [dailyView addSubview:retailLabel];
    [dailyView addSubview:calendarImageView];
    [dailyView addSubview:calendarLabel];
    if([channels count]!=0){
        CGFloat width = 320.0/[channels count];
        int index = 0;
        
        for(Channel* channel in channels){
            UILabel* channelLabel = [self createLabel:channel.channel frame:CGRectMake(0+index*width, 34, width, 50) textColor:@"#636363" font:30 backgroundColor:@"#ffffff"];
            UITapGestureRecognizer* recognizer = [[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickChannelLabel:)] autorelease];
            channelLabel.userInteractionEnabled = YES;
            [channelLabel addGestureRecognizer:recognizer];
            channelLabel.textAlignment = NSTextAlignmentCenter;
            if(index!=0){
                channelLabel.textColor = [UIColor whiteColor];
                channelLabel.backgroundColor = [UIColor blackColor];
            }
            [self.contentView addSubview:channelLabel];
            [self.channelLables addObject:channelLabel];
            index++;
        }
    }
    Channel* channel = [channels objectAtIndex:0];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"零售收入.png" frame:CGRectMake(20,114,24,24)]];
    [self.contentView addSubview:[self createLabel:@"零售收入" frame:CGRectMake(50,100,100,49) textColor:@"#4a4a4a" font:18 backgroundColor:nil]];
    self.dayAmountLabel =[self createLabel:[NSString stringWithFormat:@"%d 万元",channel.dayAmount.intValue/10000] frame:CGRectMake(200,100,100,49) textColor:@"#7f7f7f" font:18 backgroundColor:nil];
    [self.contentView addSubview:self.dayAmountLabel];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线.png" frame:CGRectMake(0,157,320,3)]];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"票数.png" frame:CGRectMake(20,174,24,24)]];
    [self.contentView addSubview:[self createLabel:@"票数" frame:CGRectMake(50,160,100,49) textColor:@"#4a4a4a" font:18 backgroundColor:nil]];
    self.docNumberLabel =[self createLabel:[NSString stringWithFormat:@"%d",channel.docNumber.intValue] frame:CGRectMake(200,160,100,49) textColor:@"#7f7f7f" font:18 backgroundColor:nil];
    [self.contentView addSubview:self.docNumberLabel];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线.png" frame:CGRectMake(0,217,320,3)]];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"附加.png" frame:CGRectMake(20,234,24,24)]];
    [self.contentView addSubview:[self createLabel:@"附加" frame:CGRectMake(50,220,100,49) textColor:@"#4a4a4a" font:18 backgroundColor:nil]];
    self.avgDocCountLabel =[self createLabel:[NSString stringWithFormat:@"%0.2f",channel.avgDocCount.doubleValue] frame:CGRectMake(200,220,100,49) textColor:@"#7f7f7f" font:18 backgroundColor:nil];
    [self.contentView addSubview:self.avgDocCountLabel];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线.png" frame:CGRectMake(0,277,320,3)]];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"件单价.png" frame:CGRectMake(20,294,24,24)]];
    [self.contentView addSubview:[self createLabel:@"件单价" frame:CGRectMake(50,280,100,49) textColor:@"#4a4a4a" font:18 backgroundColor:nil]];
    self.avgPriceLabel =[self createLabel:[NSString stringWithFormat:@"%d 元",channel.avgPrice.intValue] frame:CGRectMake(200,280,100,49) textColor:@"#7f7f7f" font:18 backgroundColor:nil];
    [self.contentView addSubview:self.avgPriceLabel];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线.png" frame:CGRectMake(0,337,320,3)]];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"单效.png" frame:CGRectMake(20,356,24,24)]];
    [self.contentView addSubview:[self createLabel:@"单效" frame:CGRectMake(50,340,100,49) textColor:@"#4a4a4a" font:18 backgroundColor:nil]];
    self.apsLabel =[self createLabel:[NSString stringWithFormat:@"%d 元",channel.aps.intValue] frame:CGRectMake(200,340,100,49) textColor:@"#7f7f7f" font:18 backgroundColor:nil];
    [self.contentView addSubview:self.apsLabel];
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-背景花纹.png" frame:CGRectMake(0,397,320,7)]];
    [self.contentView addSubview:dailyView];
    self.contentView.contentSize =
    CGSizeMake(320, 404);
}

-(void) updateChannel:(Channel*) channel{
    self.dayAmountLabel.text =[NSString stringWithFormat:@"%d 万元",channel.dayAmount.intValue/10000];
    self.docNumberLabel.text = [NSString stringWithFormat:@"%d",channel.docNumber.intValue];
    self.avgDocCountLabel.text = [NSString stringWithFormat:@"%0.2f",channel.avgDocCount.doubleValue];
    self.avgPriceLabel.text = [NSString stringWithFormat:@"%d 元",channel.avgPrice.intValue];
    self.apsLabel.text = [NSString stringWithFormat:@"%d 元",channel.aps.intValue];
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
    TT_RELEASE_SAFELY(_channelLables);
    TT_RELEASE_SAFELY(_channels);
    TT_RELEASE_SAFELY(_dayAmountLabel);
    TT_RELEASE_SAFELY(_docNumberLabel);
    TT_RELEASE_SAFELY(_avgDocCountLabel);
    TT_RELEASE_SAFELY(_avgPriceLabel);
    TT_RELEASE_SAFELY(_apsLabel);
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
