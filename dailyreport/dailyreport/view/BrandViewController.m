//
//  BrandViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "BrandViewController.h"
#import "StyleSheet.h"
#import "AppDelegate.h"


@implementation BrandViewController


@synthesize brandModel = _brandModel;

-(id) initWithBrand:(NSString*) brand{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.brand = brand;
        self.brandModel = [[[BrandModel alloc] initWithBrand:self.brand delegate:self] autorelease];
        self.index = 1;
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
    [self.brandModel load:TTURLRequestCachePolicyNone more:NO];
}

-(void)changeDateAndReload{
    [super changeDateAndReload];
    [self.brandModel load:TTURLRequestCachePolicyNone more:NO];
}
-(void)closePrompt:(id)sender{
    UIButton* promptImage = (UIButton*)sender;
    [promptImage removeFromSuperview];
    
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:VERSION forKey:FIRST_ACCESS_VERSION];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    
}

- (void) brandDidFinishLoad:(Brand*) brand channels:(NSArray*) channels weeks:(NSArray*) weeks{
     NSLog(@"%@",@"加载Brand数据成功");
    self.selectedDate = brand.date;
    

    UIView* dailyView = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH,SCREEN_HEIGHT/3)] autorelease];
    dailyView.backgroundColor =[StyleSheet colorFromHexString:[[[NSBundle mainBundle] infoDictionary] objectForKey:[NSString stringWithFormat:@"%@背景",self.brand]]];
    UIImage* retailImage = [UIImage imageNamed:@"图标-零售收入"];
    UIImageView* retailImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 34, 34)];
    retailImageView.image = retailImage;
    
    UILabel* retailLabel = [[[UILabel alloc] initWithFrame:CGRectMake(40, 0, 120, 34)] autorelease];
    retailLabel.text = [NSString stringWithFormat:@"零售额(万元)"];
    retailLabel.font = [UIFont systemFontOfSize:12];
    retailLabel.textColor = [UIColor whiteColor];
    retailLabel.backgroundColor = [UIColor clearColor];
    
    UIButton* calendarButton = [UIButton buttonWithType:UIButtonTypeCustom];
    calendarButton.frame =CGRectMake(160, 0, 34, 34);
    [calendarButton setImage:[UIImage imageNamed:@"图标-日历.png"] forState:UIControlStateNormal];
    [calendarButton addTarget:self action:@selector(openCalendar:) forControlEvents:UIControlEventTouchUpInside];
    
    
    
    
    UILabel* calendarLabel = [[[UILabel alloc] initWithFrame:CGRectMake(200, 0, 120, 34)] autorelease];
    NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:@"MM月dd日 EEEE"];
    calendarLabel.text = [dateFormatter stringFromDate:brand.date];
    calendarLabel.font = [UIFont systemFontOfSize:12];
    calendarLabel.textColor = [UIColor whiteColor];
    calendarLabel.backgroundColor = [UIColor clearColor];
    NSNumberFormatter* formatter = [[[NSNumberFormatter alloc]init] autorelease];
    formatter.numberStyle = NSNumberFormatterDecimalStyle;
    UILabel* retailAmountLabel =[self createDecimalLabel:[NSNumber numberWithDouble:brand.dayAmount.doubleValue/10000] frame:CGRectMake(0, 30, 160, 80) textColor:@"#ffffff" font:50 backgroundColor:nil textAlignment:ALIGN_CENTER];
    if(retailAmountLabel.text.length>5){
        retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:50];
    }else if(retailAmountLabel.text.length>4){
        retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:60];
    }else{
        retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:70];
    }
    
    
    NSString* retailLikeText =[NSString stringWithFormat:@"%0.1f%@",brand.dayLike.floatValue*100,@"%"];
    if(brand.dayLike.floatValue>0){
        retailLikeText = [NSString stringWithFormat:@"+%@",retailLikeText];
    }
    UILabel* retailLikeLabel = [self createLabel:retailLikeText frame:CGRectMake(40, 130, 120, 30) textColor:@"#ffffff" font:20 backgroundColor:nil];
    CGFloat offset = 0;
    for(int i=0;i<channels.count;i++){
        Channel* channel = [channels objectAtIndex:i];
        if([channel.channel isEqualToString:@"电商"]){
            offset = 5;
            [dailyView addSubview:[self createImageViewFromNamedImage:@"line.png" frame:CGRectMake(200, i*20+50+3, 90, 1.5)]];
        }
        UILabel* channelLabel=[self createLabel:channel.channel frame:CGRectMake(200, i*20+50+offset, 90, 20) textColor:@"#ffffff" font:14 backgroundColor:nil textAlignment:ALIGN_LEFT];
        UILabel* channelValue=[self createDecimalLabel:[NSNumber numberWithDouble:channel.dayAmount.doubleValue/10000] frame:CGRectMake(200, i*20+50+offset, 90, 20) textColor:@"#ffffff" font:14 backgroundColor:nil textAlignment:ALIGN_RIGHT];
        [dailyView addSubview:channelLabel];
        [dailyView addSubview:channelValue];
    }
    [dailyView addSubview:retailImageView];
    [dailyView addSubview:retailLabel];
    [dailyView addSubview:retailLikeLabel];
    //[dailyView addSubview:calendarButton];
    [dailyView addSubview:calendarLabel];
    [dailyView addSubview:retailAmountLabel];
    [self.contentView addSubview:dailyView];
    
    UIView* weeklyView = [[[UIView alloc] initWithFrame:CGRectMake(0, 166, 320, 109)] autorelease];
    weeklyView.backgroundColor = [UIColor whiteColor];
    for(int i=0;i<7;i++){
        Brand* weekBrand = [weeks objectAtIndex:i];
        UILabel* weekTitle = [[[UILabel alloc] initWithFrame:CGRectMake(2+(i)*45, 15, 45, 40)] autorelease];
        [weekTitle setNumberOfLines:2];
        #ifdef __IPHONE_6_0
            weekTitle.lineBreakMode =NSLineBreakByWordWrapping;
        #else
            weekTitle.lineBreakMode =UILineBreakModeWordWrap;
        #endif
        NSDateFormatter *df = [[NSDateFormatter alloc] init];
        [df setDateFormat:@"M/dd\nEEE"];
        weekTitle.text = [df stringFromDate:weekBrand.date];
        
        weekTitle.font = [UIFont systemFontOfSize:14];
        weekTitle.textColor = [StyleSheet colorFromHexString:@"#383838"];
        weekTitle.backgroundColor = [UIColor clearColor];
        weekTitle.textAlignment = [self getAlignment:ALIGN_CENTER];
        [weeklyView addSubview:weekTitle];
        if(weekBrand.dayAmount==nil||weekBrand.dayAmount.intValue==-1){
            continue;
        }
        UILabel* weekAmount= [self createDecimalLabel:[NSNumber numberWithDouble:weekBrand.dayAmount.doubleValue/10000] frame:CGRectMake(2+(i)*45, 60, 45, 40) textColor:@"#F55943" font:15 backgroundColor:nil textAlignment:ALIGN_CENTER];
        [weeklyView addSubview:weekAmount];
    }
    
    
    [self.contentView addSubview:weeklyView];
    
    UIView* otherView = [[[UIView alloc] initWithFrame:CGRectMake(0, 276, 320, 198)] autorelease];
    otherView.backgroundColor = [StyleSheet colorFromHexString:@"#EDEEF0"];
    
    UILabel* weekSum = [[[UILabel alloc] initWithFrame:CGRectMake(12, 11, 60, 18)] autorelease];
    weekSum.text = @"周累计";
    weekSum.font = [UIFont systemFontOfSize:18];
    weekSum.textColor = [StyleSheet colorFromHexString:@"#919191"];
    weekSum.backgroundColor = [UIColor clearColor];
    
    UILabel* weekLike = [[[UILabel alloc] initWithFrame:CGRectMake(12+160, 11, 60, 18)] autorelease];
    weekLike.text = @"周同比";
    weekLike.font = [UIFont systemFontOfSize:18];
    weekLike.textColor = [StyleSheet colorFromHexString:@"#919191"];
    weekLike.backgroundColor = [UIColor clearColor];
    
    UILabel* weekSumAmount =[self createDecimalLabel:[NSNumber numberWithDouble:brand.weekAmount.doubleValue/10000] frame:CGRectMake(12, 31, 148, 37) textColor:@"#494949" font:30 backgroundColor:nil textAlignment:ALIGN_LEFT];
    
    UILabel* weekLikeAmount = [[[UILabel alloc] initWithFrame:CGRectMake(12+160, 31, 148, 37)] autorelease];
    weekLikeAmount.text = [NSString stringWithFormat:@"%0.1f%@",brand.weekLike.floatValue*100,@"%"];
    weekLikeAmount.font = [UIFont systemFontOfSize:30];
    weekLikeAmount.textColor = [StyleSheet colorFromHexString:@"#494949"];
    weekLikeAmount.backgroundColor = [UIColor clearColor];
    
    UILabel* monthSum = [[[UILabel alloc] initWithFrame:CGRectMake(12, 74, 60, 18)] autorelease];
    monthSum.text = @"月累计";
    monthSum.font = [UIFont systemFontOfSize:18];
    monthSum.textColor = [StyleSheet colorFromHexString:@"#919191"];
    monthSum.backgroundColor = [UIColor clearColor];
    
    UILabel* monthLike = [[[UILabel alloc] initWithFrame:CGRectMake(12+160, 74, 60, 18)] autorelease];
    monthLike.text = @"月同比";
    monthLike.font = [UIFont systemFontOfSize:18];
    monthLike.textColor = [StyleSheet colorFromHexString:@"#919191"];
    monthLike.backgroundColor = [UIColor clearColor];
    
    UILabel* monthSumAmount =[self createDecimalLabel:[NSNumber numberWithDouble:brand.monthAmount.doubleValue/10000] frame:CGRectMake(12, 94, 148, 38) textColor:@"#494949" font:30 backgroundColor:nil textAlignment:ALIGN_LEFT];
    
    UILabel* monthLikeAmount = [[[UILabel alloc] initWithFrame:CGRectMake(12+160, 94, 148, 38)] autorelease];
    monthLikeAmount.text = [NSString stringWithFormat:@"%0.1f%@",brand.monthLike.floatValue*100,@"%"];
    monthLikeAmount.font = [UIFont systemFontOfSize:30];
    monthLikeAmount.textColor = [StyleSheet colorFromHexString:@"#494949"];
    monthLikeAmount.backgroundColor = [UIColor clearColor];
    
    
    UILabel* yearSum = [[[UILabel alloc] initWithFrame:CGRectMake(12, 138, 60, 18)] autorelease];
    yearSum.text = @"年累计";
    yearSum.font = [UIFont systemFontOfSize:18];
    yearSum.textColor = [StyleSheet colorFromHexString:@"#919191"];
    yearSum.backgroundColor = [UIColor clearColor];
    
    UILabel* yearLike = [[[UILabel alloc] initWithFrame:CGRectMake(12+160, 138, 60, 18)] autorelease];
    yearLike.text = @"年同比";
    yearLike.font = [UIFont systemFontOfSize:18];
    yearLike.textColor = [StyleSheet colorFromHexString:@"#919191"];
    yearLike.backgroundColor = [UIColor clearColor];
    
    UILabel* yearSumAmount =[self createDecimalLabel:[NSNumber numberWithDouble:brand.yearAmount.doubleValue/10000] frame:CGRectMake(12, 158, 148, 38) textColor:@"#494949" font:30 backgroundColor:nil textAlignment:ALIGN_LEFT];
        
    UILabel* yearLikeAmount = [[[UILabel alloc] initWithFrame:CGRectMake(12+160, 158, 148, 38)] autorelease];
    yearLikeAmount.text = [NSString stringWithFormat:@"%0.1f%@",brand.yearLike.floatValue*100,@"%"];
    yearLikeAmount.font = [UIFont systemFontOfSize:30];
    yearLikeAmount.textColor = [StyleSheet colorFromHexString:@"#494949"];
    yearLikeAmount.backgroundColor = [UIColor clearColor];
    [otherView addSubview:weekSum];
    [otherView addSubview:weekSumAmount];
    [otherView addSubview:weekLike];
    [otherView addSubview:weekLikeAmount];
    
    [otherView addSubview:monthSum];
    [otherView addSubview:monthSumAmount];
    [otherView addSubview:monthLike];
    [otherView addSubview:monthLikeAmount];
    
    [otherView addSubview:yearSum];
    [otherView addSubview:yearSumAmount];
    [otherView addSubview:yearLike];
    [otherView addSubview:yearLikeAmount];
    [self.contentView addSubview:otherView];
    self.contentView.contentSize =
    CGSizeMake(320, 474);
    
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    NSString* firstAccessVersion = [defaults objectForKey:FIRST_ACCESS_VERSION];
    if(firstAccessVersion==nil||![firstAccessVersion isEqualToString:VERSION]){
        UIButton* promptImage = [UIButton buttonWithType:UIButtonTypeCustom];
        promptImage.frame =self.contentView.frame;        [promptImage setImage:[UIImage imageNamed:@"首次进入提示"] forState:UIControlStateNormal];
        [promptImage addTarget:self action:@selector(closePrompt:) forControlEvents:UIControlEventTouchUpInside];
        promptImage.backgroundColor =[UIColor colorWithRed:0 green:0 blue:0 alpha:0.3f];
        [self.contentView addSubview:promptImage];
    }
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

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc
{
    _brandModel.delegate = nil;
    TT_RELEASE_SAFELY(_brandModel);
    [super dealloc];
}
@end
