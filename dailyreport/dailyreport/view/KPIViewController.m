//
//  KPIViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-5-20.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "KPIViewController.h"
#import "StyleSheet.h"
#import "AppDelegate.h"
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
-(void)changeDateAndReload{
    [super changeDateAndReload];
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
        l.backgroundColor = [StyleSheet colorFromHexString:TAB_COLOR];
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
    self.selectedDate = date;
    self.channels = channels;

    UIView* dailyView =[self createDailyView:@"图标-关键指标" label:@"关键指标"];
    int channelFontSize = [self getFont:20 ip6Offset:2 ip6pOffset:4];
    int labelFontSize =[self getFont:18 ip6Offset:2 ip6pOffset:4];
    
    NSMutableArray* ca = [NSMutableArray array];
    for(Channel* channel in channels){
        if(![channel.channel isEqualToString:@"电商"]){
            [ca addObject:channel];
        }
    }
    CGFloat t = 0;
    if([ca count]!=0){
        CGFloat width = SCREEN_WIDTH/[ca count];
        int index = 0;
        
        for(Channel* channel in ca){
            if([channel.channel isEqualToString:@"电商"]){
                continue;
            }
            CGFloat x = index*width;
            CGFloat realWidth = width-0.5;
            if(index!=0){
                x =x +0.5;
            }
            if(index==[ca count]-1){
                realWidth = SCREEN_WIDTH-t;
            }
            t+=width;
            UILabel* channelLabel = [self createLabel:channel.channel frame:CGRectMake(x, dailyView.frame.size.height, realWidth, [self getTabHeight]) textColor:@"#636363" font:channelFontSize backgroundColor:@"#ffffff"];
            UITapGestureRecognizer* recognizer = [[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickChannelLabel:)] autorelease];
            channelLabel.userInteractionEnabled = YES;
            [channelLabel addGestureRecognizer:recognizer];
            channelLabel.textAlignment = [self getAlignment:ALIGN_CENTER];
            if(index!=0){
                channelLabel.textColor = [UIColor whiteColor];
                channelLabel.backgroundColor = [StyleSheet colorFromHexString:TAB_COLOR];
            }
            [self.contentView addSubview:channelLabel];
            [self.channelLables addObject:channelLabel];
            index++;
        }
    }
    Channel* channel = nil;
    if(ca.count>0){
       channel  = [ca objectAtIndex:0];
    }else{
        channel = [[[Channel alloc] init] autorelease];
    }
    CGFloat hOffset = dailyView.frame.size.height+SCREEN_HEIGHT*2/48+[self getTabHeight];
    
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
    self.dayAmountLabel =[self createDecimalLabel:[NSNumber numberWithDouble:channel.dayAmount.doubleValue/10000] unit:@"万元" frame:CGRectMake(SCREEN_WIDTH*11/16,hOffset,SCREEN_WIDTH*5/16,rowHeight) textColor:@"#7f7f7f" font:labelFontSize backgroundColor:nil textAlignment:ALIGN_LEFT];

    [self.contentView addSubview:self.dayAmountLabel];
    hOffset+=rowHeight;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线" frame:CGRectMake(0,hOffset,SCREEN_WIDTH,3)]];
    hOffset+=3;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"票数" point:CGPointMake(20,hOffset+rowImageHeight/2)]];
    [self.contentView addSubview:[self createLabel:@"票数" frame:CGRectMake(labelOffset,hOffset,100,rowHeight) textColor:@"#4a4a4a" font:labelFontSize backgroundColor:nil]];
    self.docNumberLabel =[self createDecimalLabel:[NSNumber numberWithInt:channel.docNumber.intValue] frame:CGRectMake(SCREEN_WIDTH*11/16,hOffset,SCREEN_WIDTH*5/16,rowHeight) textColor:@"#7f7f7f" font:labelFontSize backgroundColor:nil textAlignment:ALIGN_LEFT];
    [self.contentView addSubview:self.docNumberLabel];
    hOffset+=rowHeight;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线" frame:CGRectMake(0,hOffset,SCREEN_WIDTH,3)]];
    hOffset+=3;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"附加" point:CGPointMake(20,hOffset+rowImageHeight/2)]];
    [self.contentView addSubview:[self createLabel:@"附加" frame:CGRectMake(labelOffset,hOffset,100,rowHeight)  textColor:@"#4a4a4a" font:18 backgroundColor:nil]];
    self.avgDocCountLabel =[self createLabel:[NSString stringWithFormat:@"%0.1f",channel.avgDocCount.doubleValue] frame:CGRectMake(SCREEN_WIDTH*11/16,hOffset,SCREEN_WIDTH*5/16,rowHeight) textColor:@"#7f7f7f" font:labelFontSize backgroundColor:nil textAlignment:ALIGN_LEFT];
    [self.contentView addSubview:self.avgDocCountLabel];
    hOffset+=rowHeight;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线" frame:CGRectMake(0,hOffset,SCREEN_WIDTH,3)]];
     hOffset+=3;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"件单价" point:CGPointMake(20,hOffset+rowImageHeight/2)]];
    [self.contentView addSubview:[self createLabel:@"件单价" frame:CGRectMake(labelOffset,hOffset,100,rowHeight) textColor:@"#4a4a4a" font:labelFontSize backgroundColor:nil]];
    self.avgPriceLabel =[self createDecimalLabel:[NSNumber numberWithInt:channel.avgPrice.intValue] unit:@"元" frame:CGRectMake(SCREEN_WIDTH*11/16,hOffset,SCREEN_WIDTH*5/16,rowHeight) textColor:@"#7f7f7f" font:18 backgroundColor:nil textAlignment:ALIGN_LEFT];
    [self.contentView addSubview:self.avgPriceLabel];
    hOffset+=rowHeight;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-分割线" frame:CGRectMake(0,hOffset,SCREEN_WIDTH,3)]];
    hOffset+=3;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"单效" point:CGPointMake(20,hOffset+rowImageHeight/2)]];
    [self.contentView addSubview:[self createLabel:@"坪效" frame:CGRectMake(labelOffset,hOffset,100,rowHeight)  textColor:@"#4a4a4a" font:labelFontSize backgroundColor:nil]];
    self.apsLabel =[self createDecimalLabel:[NSNumber numberWithInt:channel.aps.intValue] unit:@"元" frame:CGRectMake(SCREEN_WIDTH*11/16,hOffset,SCREEN_WIDTH*5/16,rowHeight)textColor:@"#7f7f7f" font:labelFontSize backgroundColor:nil textAlignment:ALIGN_LEFT];
    [self.contentView addSubview:self.apsLabel];
    hOffset+=rowHeight;
    [self.contentView addSubview:[self createImageViewFromNamedImage:@"关键指标-背景花纹" frame:CGRectMake(0,hOffset,SCREEN_WIDTH,7)]];
    hOffset+=7;
    [self.contentView addSubview:dailyView];
    self.contentView.contentSize =
    CGSizeMake(SCREEN_WIDTH, hOffset);
}

-(void) updateChannel:(Channel*) channel{
    NSNumberFormatter* formatter1 = [[[NSNumberFormatter alloc]init] autorelease];
    formatter1.numberStyle = NSNumberFormatterDecimalStyle;
    
    NSNumberFormatter* formatter = [[[NSNumberFormatter alloc]init] autorelease];
    formatter.numberStyle = NSNumberFormatterDecimalStyle;
    
    if(channel.dayAmount.doubleValue>=10000){
        [formatter1 setMinimumFractionDigits:0];
        [formatter1 setMaximumFractionDigits:0];
        self.dayAmountLabel.text = [NSString stringWithFormat:@"%@ %@",[formatter1 stringFromNumber:[NSNumber numberWithDouble:channel.dayAmount.doubleValue/10000]],@"万元"];
    }else{
        [formatter1 setMinimumFractionDigits:2];
        [formatter1 setMaximumFractionDigits:2];
        self.dayAmountLabel.text = [NSString stringWithFormat:@"%@ %@",[formatter1 stringFromNumber:[NSNumber numberWithDouble:channel.dayAmount.doubleValue/10000]],@"万元"];
    }
    
    
    self.docNumberLabel.text =
    [NSString stringWithFormat:@"%@",[formatter stringFromNumber:[NSNumber numberWithInt:channel.docNumber.intValue]]];

    self.avgDocCountLabel.text = [NSString stringWithFormat:@"%0.1f",channel.avgDocCount.doubleValue];
    self.avgPriceLabel.text =[NSString stringWithFormat:@"%@ %@",[formatter stringFromNumber:[NSNumber numberWithInt:channel.avgPrice.intValue]],@"元"];
    
    self.apsLabel.text = [NSString stringWithFormat:@"%@ %@",[formatter stringFromNumber:[NSNumber numberWithInt:channel.aps.intValue]],@"元"];
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
