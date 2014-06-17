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
    UIView* dailyView = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 166)] autorelease];
    NSString* imageName = [NSString stringWithFormat:@"每日收入%@背景.png",self.brand];
    dailyView.backgroundColor =[UIColor colorWithPatternImage:[UIImage imageNamed:imageName]];
    UIImage* retailImage = [UIImage imageNamed:@"图标-零售收入.png"];
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
    
    UILabel* retailAmountLabel = [[[UILabel alloc] initWithFrame:CGRectMake(20, 50, 120, 80)] autorelease];
    retailAmountLabel.text = [NSString stringWithFormat:@"%d",brand.dayAmount.intValue/10000];
    if(brand.dayAmount.intValue/10000>=1000){
        retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:60];
    }else{
        retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:70];
    }
    
    retailAmountLabel.textColor = [UIColor whiteColor];
    retailAmountLabel.backgroundColor = [UIColor clearColor];
    
    UILabel* retailLikeLabel = [self createLabel:[NSString stringWithFormat:@"%0.1f%@",brand.dayLike.floatValue*100,@"%"] frame:CGRectMake(40, 130, 120, 30) textColor:@"#ffffff" font:20 backgroundColor:nil];
    for(int i=0;i<channels.count;i++){
        Channel* channel = [channels objectAtIndex:i];
        UILabel* channelLabel=[self createLabel:channel.channel frame:CGRectMake(200, i*20+50, 90, 20) textColor:@"#ffffff" font:14 backgroundColor:nil textAlignment:NSTextAlignmentLeft];
        UILabel* channelValue=[self createDecimalLabel:[NSNumber numberWithInt:channel.dayAmount.intValue/10000] frame:CGRectMake(200, i*20+50, 90, 20) textColor:@"#ffffff" font:14 backgroundColor:nil textAlignment:NSTextAlignmentRight];
        [dailyView addSubview:channelLabel];
        [dailyView addSubview:channelValue];
    }
    [dailyView addSubview:retailImageView];
    [dailyView addSubview:retailLabel];
    [dailyView addSubview:retailLikeLabel];
    [dailyView addSubview:calendarButton];
    [dailyView addSubview:calendarLabel];
    [dailyView addSubview:retailAmountLabel];
    [self.contentView addSubview:dailyView];
    
    UIView* weeklyView = [[[UIView alloc] initWithFrame:CGRectMake(0, 166, 320, 109)] autorelease];
    weeklyView.backgroundColor = [UIColor whiteColor];
    for(int i=0;i<7;i++){
        Brand* weekBrand = [weeks objectAtIndex:i];
        UILabel* weekTitle = [[[UILabel alloc] initWithFrame:CGRectMake(20+(i)*40, 15, 40, 40)] autorelease];
        [weekTitle setNumberOfLines:2];
        weekTitle.lineBreakMode =NSLineBreakByWordWrapping;
        NSDateFormatter *df = [[NSDateFormatter alloc] init];
        [df setDateFormat:@"M/dd\nEEE"];
        weekTitle.text = [df stringFromDate:weekBrand.date];
        
        weekTitle.font = [UIFont systemFontOfSize:14];
        weekTitle.textColor = [StyleSheet colorFromHexString:@"#383838"];
        weekTitle.backgroundColor = [UIColor clearColor];
        weekTitle.textAlignment = NSTextAlignmentCenter;
        [weeklyView addSubview:weekTitle];
        if(weekBrand.dayAmount==nil||weekBrand.dayAmount.intValue==-1){
            continue;
        }
        UILabel* weekAmount = [[[UILabel alloc] initWithFrame:CGRectMake(20+(i)*40, 60, 40, 40)] autorelease];
        weekAmount.text = [NSString stringWithFormat:@"%d",weekBrand.dayAmount.intValue/10000];
        weekAmount.textAlignment = NSTextAlignmentCenter;
        weekAmount.font = [UIFont systemFontOfSize:18];
        weekAmount.textColor = [StyleSheet colorFromHexString:@"#F55943"];
        weekAmount.backgroundColor = [UIColor clearColor];
        [weeklyView addSubview:weekAmount];
    }
    
    
    [self.contentView addSubview:weeklyView];
    
    UIView* otherView = [[[UIView alloc] initWithFrame:CGRectMake(0, 276, 320, 157)] autorelease];
    otherView.backgroundColor = [StyleSheet colorFromHexString:@"#EDEEF0"];
    
    UILabel* weekSum = [[[UILabel alloc] initWithFrame:CGRectMake(12, 12, 60, 20)] autorelease];
    weekSum.text = @"周累计";
    weekSum.font = [UIFont systemFontOfSize:20];
    weekSum.textColor = [StyleSheet colorFromHexString:@"#919191"];
    weekSum.backgroundColor = [UIColor clearColor];
    
    UILabel* weekLike = [[[UILabel alloc] initWithFrame:CGRectMake(12+160, 12, 60, 20)] autorelease];
    weekLike.text = @"周同比";
    weekLike.font = [UIFont systemFontOfSize:20];
    weekLike.textColor = [StyleSheet colorFromHexString:@"#919191"];
    weekLike.backgroundColor = [UIColor clearColor];
    
    UILabel* weekSumAmount = [[[UILabel alloc] initWithFrame:CGRectMake(12, 35, 140, 40)] autorelease];
    weekSumAmount.text = [NSString stringWithFormat:@"%d",brand.weekAmount.intValue/10000];
    weekSumAmount.font = [UIFont systemFontOfSize:32];
    weekSumAmount.textColor = [StyleSheet colorFromHexString:@"#494949"];
    weekSumAmount.backgroundColor = [UIColor clearColor];
    
    UILabel* weekLikeAmount = [[[UILabel alloc] initWithFrame:CGRectMake(12+160, 35, 140, 40)] autorelease];
    weekLikeAmount.text = [NSString stringWithFormat:@"%0.1f%@",brand.weekLike.floatValue*100,@"%"];
    weekLikeAmount.font = [UIFont systemFontOfSize:32];
    weekLikeAmount.textColor = [StyleSheet colorFromHexString:@"#494949"];
    weekLikeAmount.backgroundColor = [UIColor clearColor];
    
    UILabel* yearSum = [[[UILabel alloc] initWithFrame:CGRectMake(12, 82, 60, 20)] autorelease];
    yearSum.text = @"年累计";
    yearSum.font = [UIFont systemFontOfSize:20];
    yearSum.textColor = [StyleSheet colorFromHexString:@"#919191"];
    yearSum.backgroundColor = [UIColor clearColor];
    
    UILabel* yearLike = [[[UILabel alloc] initWithFrame:CGRectMake(12+160, 82, 60, 20)] autorelease];
    yearLike.text = @"年同比";
    yearLike.font = [UIFont systemFontOfSize:20];
    yearLike.textColor = [StyleSheet colorFromHexString:@"#919191"];
    yearLike.backgroundColor = [UIColor clearColor];
    
    UILabel* yearSumAmount = [[[UILabel alloc] initWithFrame:CGRectMake(12, 105, 140, 40)] autorelease];
    yearSumAmount.text = [NSString stringWithFormat:@"%d",brand.yearAmount.intValue/10000];
    yearSumAmount.font = [UIFont systemFontOfSize:32];
    yearSumAmount.textColor = [StyleSheet colorFromHexString:@"#494949"];
    yearSumAmount.backgroundColor = [UIColor clearColor];
    
    UILabel* yearLikeAmount = [[[UILabel alloc] initWithFrame:CGRectMake(12+160, 105, 140, 40)] autorelease];
    yearLikeAmount.text = [NSString stringWithFormat:@"%0.1f%@",brand.yearLike.floatValue*100,@"%"];
    yearLikeAmount.font = [UIFont systemFontOfSize:32];
    yearLikeAmount.textColor = [StyleSheet colorFromHexString:@"#494949"];
    yearLikeAmount.backgroundColor = [UIColor clearColor];
    [otherView addSubview:weekSum];
    [otherView addSubview:weekSumAmount];
    [otherView addSubview:weekLike];
    [otherView addSubview:weekLikeAmount];
    
    [otherView addSubview:yearSum];
    [otherView addSubview:yearSumAmount];
    [otherView addSubview:yearLike];
    [otherView addSubview:yearLikeAmount];
    [self.contentView addSubview:otherView];
    self.contentView.contentSize =
    CGSizeMake(320, 432);
    
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    NSString* firstAccessVersion = [defaults objectForKey:FIRST_ACCESS_VERSION];
    if(firstAccessVersion==nil||![firstAccessVersion isEqualToString:VERSION]){
        UIButton* promptImage = [UIButton buttonWithType:UIButtonTypeCustom];
        promptImage.frame =self.contentView.frame;        [promptImage setImage:[UIImage imageNamed:@"首次进入提示.png"] forState:UIControlStateNormal];
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
