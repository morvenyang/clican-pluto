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
#import "CannotCancelUISwipeGestureRecognizer.h"
#ifdef __IPHONE_7_0
#import <JavaScriptCore/JavaScriptCore.h>
#endif
@implementation BrandViewController


@synthesize brandModel = _brandModel;
@synthesize webLineChartView = _webLineChartView;

@synthesize dailyLineChart = _dailyLineChart;
@synthesize weeklyLineChart = _weeklyLineChart;
@synthesize monthlyLineChart = _monthlyLineChart;
@synthesize yearlyLineChart = _yearlyLineChart;
@synthesize lineChart = _lineChart;

@synthesize dailyButton = _dailyButton;
@synthesize weeklyButton = _weeklyButton;
@synthesize monthlyButton = _monthlyButton;
@synthesize yearlyButton = _yearlyButton;
@synthesize infoView = _infoView;
@synthesize periodImageView = _periodImageView;
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
-(void)handleGesture:(UISwipeGestureRecognizer*)gestureRecognizer{
    UISwipeGestureRecognizerDirection direction =gestureRecognizer.direction;
    if(direction==UISwipeGestureRecognizerDirectionLeft){
        NSLog(@"left");
        NSString* url = [NSString stringWithFormat:@"peacebird://kpi/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
        TTOpenURL(url);
        return;
    }
    
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

- (void) brandDidFinishLoad:(Brand*) brand channels:(NSArray*) channels dailyLineChart:(NSString*)dailyLineChart weeklyLineChart:(NSString*)weeklyLineChart monthlyLineChart:(NSString*)monthlyLineChart yearlyLineChart:(NSString*)yearlyLineChart{
     NSLog(@"%@",@"加载Brand数据成功");
    self.selectedDate = brand.date;
    self.dailyLineChart = dailyLineChart;
    self.weeklyLineChart = weeklyLineChart;
    self.monthlyLineChart = monthlyLineChart;
    self.yearlyLineChart = yearlyLineChart;
    self.lineChart = self.dailyLineChart;
    int labelFontSize = [self getFont:12 ip6Offset:2 ip6pOffset:4];
    int channelFontSize = [self getFont:14 ip6Offset:2 ip6pOffset:4];
    NSLog(@"%f",SCREEN_WIDTH);
    CGFloat dailyViewHeight =SCREEN_HEIGHT/3;
    if(IS_IPHONE6){
        dailyViewHeight = dailyViewHeight-40;
    }else if(IS_IPHONE6_PLUS){
        dailyViewHeight = dailyViewHeight-60;
    }
    
    
    UIView* dailyView = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH,dailyViewHeight)] autorelease];
    dailyView.backgroundColor =[StyleSheet colorFromHexString:[[[NSBundle mainBundle] infoDictionary] objectForKey:[NSString stringWithFormat:@"%@背景",self.brand]]];
    UIImage* retailImage = [UIImage imageNamed:@"图标-零售收入"];
    UIImageView* retailImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, retailImage.size.width, retailImage.size.height)];
    retailImageView.image = retailImage;
    
    UILabel* retailLabel = [[[UILabel alloc] initWithFrame:CGRectMake(40, 0, 120, retailImage.size.height)] autorelease];
    retailLabel.text = [NSString stringWithFormat:@"零售额(万元)"];
    retailLabel.font = [UIFont systemFontOfSize:labelFontSize];
    retailLabel.textColor = [UIColor whiteColor];
    retailLabel.backgroundColor = [UIColor clearColor];
    
    UILabel* calendarLabel = [[[UILabel alloc] initWithFrame:CGRectMake(SCREEN_WIDTH/2, 0, SCREEN_WIDTH/2-30, retailImage.size.height)] autorelease];
    NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:@"MM月dd日 EEEE"];
    calendarLabel.text = [dateFormatter stringFromDate:brand.date];
    calendarLabel.font = [UIFont systemFontOfSize:labelFontSize];
    calendarLabel.textColor = [UIColor whiteColor];
    calendarLabel.backgroundColor = [UIColor clearColor];
    calendarLabel.textAlignment = NSTextAlignmentRight;
    NSNumberFormatter* formatter = [[[NSNumberFormatter alloc]init] autorelease];
    formatter.numberStyle = NSNumberFormatterDecimalStyle;
    UILabel* retailAmountLabel =[self createDecimalLabel:[NSNumber numberWithDouble:brand.dayAmount.doubleValue/10000] frame:CGRectMake(0, 30, SCREEN_WIDTH/2, 80) textColor:@"#ffffff" font:50 backgroundColor:nil textAlignment:ALIGN_CENTER];
    
    
    
    if(IS_IPHONE4||IS_IPHONE5){
        if(retailAmountLabel.text.length>5){
            retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:50];
        }else if(retailAmountLabel.text.length>4){
            retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:60];
        }else{
            retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:70];
        }
    }else if(IS_IPHONE6){
        if(retailAmountLabel.text.length>5){
            retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:55];
        }else if(retailAmountLabel.text.length>4){
            retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:60];
        }else{
            retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:65];
        }
    }else{
        if(retailAmountLabel.text.length>5){
            retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:60];
        }else if(retailAmountLabel.text.length>4){
            retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:70];
        }else{
            retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:80];
        }
    }
    
    NSString* retailLikeText =[NSString stringWithFormat:@"%0.1f%@",brand.dayLike.floatValue*100,@"%"];
    if(brand.dayLike.floatValue>0){
        retailLikeText = [NSString stringWithFormat:@"+%@",retailLikeText];
    }
    UILabel* retailLikeLabel = [self createLabel:retailLikeText frame:CGRectMake(40, 130, SCREEN_WIDTH/2-40, 30) textColor:@"#ffffff" font:labelFontSize+8 backgroundColor:nil];
    CGFloat offset = 0;
    
    for(int i=0;i<channels.count;i++){
        Channel* channel = [channels objectAtIndex:i];
        if([channel.channel isEqualToString:@"电商"]){
            offset = 5;
            [dailyView addSubview:[self createImageViewFromNamedImage:@"line.png" frame:CGRectMake(SCREEN_WIDTH*5/8, i*20+50+3, SCREEN_WIDTH*3/8-30, 1.5)]];
        }
        UILabel* channelLabel=[self createLabel:channel.channel frame:CGRectMake(SCREEN_WIDTH*5/8, i*20+50+offset, SCREEN_WIDTH*3/8-30, 20) textColor:@"#ffffff" font:channelFontSize backgroundColor:nil textAlignment:ALIGN_LEFT];
        UILabel* channelValue=[self createDecimalLabel:[NSNumber numberWithDouble:channel.dayAmount.doubleValue/10000] frame:CGRectMake(SCREEN_WIDTH*5/8, i*20+50+offset, SCREEN_WIDTH*3/8-30, 20) textColor:@"#ffffff" font:channelFontSize backgroundColor:nil textAlignment:ALIGN_RIGHT];
        [dailyView addSubview:channelLabel];
        [dailyView addSubview:channelValue];
    }
    [dailyView addSubview:retailImageView];
    [dailyView addSubview:retailLabel];
    [dailyView addSubview:retailLikeLabel];
    [dailyView addSubview:calendarLabel];
    [dailyView addSubview:retailAmountLabel];
    [self.contentView addSubview:dailyView];
    
    
    UIImage* dayImage = [UIImage imageNamed:@"day"];
    CGFloat space = (SCREEN_WIDTH-dayImage.size.width*4)/12;
    CGFloat wOffset = space;
    CGFloat periodHeight = dayImage.size.height*3;
    CGFloat yOffset = dailyView.frame.size.height;
    self.dailyButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.dailyButton setImage:[UIImage imageNamed:@"day_01"] forState:UIControlStateNormal];
    [self.dailyButton addTarget:self action:@selector(changePeriod:) forControlEvents:UIControlEventTouchUpInside];
    self.dailyButton.frame = CGRectMake(wOffset, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height);
    [self.contentView addSubview:self.dailyButton];
    NSLog(@"%f",wOffset+dayImage.size.width);
    [self.contentView addSubview:[self createLabel:@"天" frame:CGRectMake(wOffset+dayImage.size.width+3, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height) textColor:@"#000000" font:labelFontSize backgroundColor:nil]];
    
    UIImage* dayArrow = [UIImage imageNamed:@"day_arrow"];
    self.periodImageView =[self createImageViewFromImage:dayArrow frame:CGRectMake(0, yOffset+periodHeight/3+dayImage.size.height, SCREEN_WIDTH, dayArrow.size.height)];
    [self.contentView addSubview:self.periodImageView];
    
    wOffset+=space*3+dayImage.size.width;
    self.weeklyButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.weeklyButton setImage:[UIImage imageNamed:@"week"] forState:UIControlStateNormal];
    [self.weeklyButton addTarget:self action:@selector(changePeriod:) forControlEvents:UIControlEventTouchUpInside];
    self.weeklyButton.frame = CGRectMake(wOffset, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height);
    [self.contentView addSubview:self.weeklyButton];
    NSLog(@"%f",wOffset+dayImage.size.width);
    [self.contentView addSubview:[self createLabel:@"周" frame:CGRectMake(wOffset+dayImage.size.width+3, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height) textColor:@"#000000" font:labelFontSize backgroundColor:nil]];
    
    
    wOffset+=space*3+dayImage.size.width;
    self.monthlyButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.monthlyButton setImage:[UIImage imageNamed:@"month"] forState:UIControlStateNormal];
    [self.monthlyButton addTarget:self action:@selector(changePeriod:) forControlEvents:UIControlEventTouchUpInside];
    self.monthlyButton.frame = CGRectMake(wOffset, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height);
    [self.contentView addSubview:self.monthlyButton];
    NSLog(@"%f",wOffset+dayImage.size.width);
    [self.contentView addSubview:[self createLabel:@"月" frame:CGRectMake(wOffset+dayImage.size.width+3, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height) textColor:@"#000000" font:labelFontSize backgroundColor:nil]];
    
    wOffset+=space*3+dayImage.size.width;
    self.yearlyButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.yearlyButton setImage:[UIImage imageNamed:@"year"] forState:UIControlStateNormal];
    [self.yearlyButton addTarget:self action:@selector(changePeriod:) forControlEvents:UIControlEventTouchUpInside];
    self.yearlyButton.frame = CGRectMake(wOffset, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height);
    [self.contentView addSubview:self.yearlyButton];
    NSLog(@"%f",wOffset+dayImage.size.width);
    [self.contentView addSubview:[self createLabel:@"年" frame:CGRectMake(wOffset+dayImage.size.width+3, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height) textColor:@"#000000" font:labelFontSize backgroundColor:nil]];
    yOffset+=periodHeight*2/3;
    _chartYOffset = yOffset;
    CGFloat infoHeight = SCREEN_HEIGHT/12;
    self.webLineChartView = [[[UIWebView alloc] initWithFrame:CGRectMake(0,_chartYOffset+infoHeight,SCREEN_WIDTH,SCREEN_HEIGHT-64-30-_chartYOffset-infoHeight)] autorelease];
    self.webLineChartView.scalesPageToFit=YES;
    self.webLineChartView.userInteractionEnabled =YES;
    self.webLineChartView.delegate = self;
    CannotCancelUISwipeGestureRecognizer* swipeGestureLeft = [[[CannotCancelUISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleGesture:)] autorelease];
    swipeGestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    [self.webLineChartView.scrollView addGestureRecognizer:swipeGestureLeft];

    [self.contentView addSubview:self.webLineChartView];
    if(self.lineChart!=nil&&![self.lineChart isEqualToString:@"[\n\n]"]){
        [self generateInfoView:self.lineChart index:-1];
        [self generateLikeChart:self.lineChart];
    }else{
        TTAlert(@"没有相关图标数据");
    }
    //self.contentView.contentSize = CGSizeMake(SCREEN_WIDTH, 1000);
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    NSString* firstAccessVersion = [defaults objectForKey:FIRST_ACCESS_VERSION];
    if(firstAccessVersion==nil||![firstAccessVersion isEqualToString:VERSION]){
        UIButton* promptImage = [UIButton buttonWithType:UIButtonTypeCustom];
        UIImage* image =[UIImage imageNamed:@"首次进入提示"];
        
        [promptImage setImage:image forState:UIControlStateNormal];
        promptImage.frame =self.contentView.frame;
        [promptImage addTarget:self action:@selector(closePrompt:) forControlEvents:UIControlEventTouchUpInside];
        promptImage.backgroundColor =[UIColor colorWithRed:0 green:0 blue:0 alpha:0.3f];
        [self.contentView addSubview:promptImage];
    }
}

-(void)generateInfoView:(NSString*)dataProvider index:(int) index{
    if(self.infoView){
        [self.infoView removeFromSuperview];
    }
    int labelFontSize = [self getFont:12 ip6Offset:2 ip6pOffset:4];
    int amountFontSize = [self getFont:19 ip6Offset:3 ip6pOffset:6];
   
    CGFloat infoHeight =SCREEN_HEIGHT/12;
    self.infoView = [[UIView alloc] initWithFrame:CGRectMake(0, _chartYOffset, SCREEN_WIDTH, infoHeight)];

    [self.contentView addSubview:self.infoView];
    NSArray* jsonArray = [NSJSONSerialization JSONObjectWithData:[dataProvider dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:nil];
    if(index==-1){
        index= jsonArray.count-1;
    }
    NSDictionary* jsonObj = [jsonArray objectAtIndex:index];
    NSString* fullDateStr = [jsonObj objectForKey:@"fullDateStr"];
    NSNumber* amount =[jsonObj objectForKey:@"amount"];
    NSNumber* like =[jsonObj objectForKey:@"like"];
    CGFloat leftOffset = 30;

    
    NSString* color = [[[NSBundle mainBundle] infoDictionary] objectForKey:[NSString stringWithFormat:@"%@背景",self.brand]];
    [self.infoView addSubview:[self createLabel:fullDateStr frame:CGRectMake(10, 0, SCREEN_WIDTH/2-leftOffset, infoHeight) textColor:@"#000000" font:labelFontSize backgroundColor:nil]];
    CGFloat wOffset =SCREEN_WIDTH/2-leftOffset;
    [self.infoView addSubview:[self createLabel:@"收入" frame:CGRectMake(wOffset, 0, 25*SCREEN_WIDTH/320, infoHeight) textColor:@"#000000" font:labelFontSize backgroundColor:nil]];
    wOffset+=25*SCREEN_WIDTH/320;
    UILabel* amountLabel=[self createDecimalLabel:amount frame:CGRectMake(wOffset, 0, 72*SCREEN_WIDTH/320, infoHeight) textColor:color font:amountFontSize backgroundColor:nil textAlignment:ALIGN_LEFT];
    
    [self.infoView addSubview:amountLabel];
    
    wOffset+=72*SCREEN_WIDTH/320;
    [self.infoView addSubview:[self createLabel:@"同比" frame:CGRectMake(wOffset, 0, 25*SCREEN_WIDTH/320, infoHeight) textColor:@"#000000" font:labelFontSize backgroundColor:nil]];
    wOffset+=25*SCREEN_WIDTH/320;
    UILabel* label = [self createDecimalLabel:like unit:@"%" frame:CGRectMake(wOffset, 0, SCREEN_WIDTH-wOffset, infoHeight) textColor:color font:amountFontSize backgroundColor:nil textAlignment:ALIGN_LEFT];
    if(like.floatValue>0){
        label.text = [NSString stringWithFormat:@"+%@",label.text];
    }
    [self.infoView addSubview:label];
}
-(void)generateLikeChart:(NSString*)dataProvider{
    NSString* htmlPath = [[NSBundle mainBundle] pathForResource:@"dailyLineChart" ofType:@"html" inDirectory:@"web"];
    NSString* html = [NSString stringWithContentsOfFile:htmlPath encoding:NSUTF8StringEncoding error:nil];
    
    html=[html stringByReplacingOccurrencesOfString:@"$dataProvider" withString:dataProvider];
    html=[html stringByReplacingOccurrencesOfString:@"$width" withString:@"1000"];
    CGFloat h = SCREEN_HEIGHT;
    CGFloat bottom = 70;
    CGFloat top = 10;
    if(IS_IPHONE5){
        h+=10;
        bottom = 70;
    }else if(IS_IPHONE6){
        bottom = 70;
        h+=120;
    }else if(IS_IPHONE6_PLUS){
        bottom = 60;
        h+=60;
        top = -10;
    }
    html=[html stringByReplacingOccurrencesOfString:@"$height" withString:[NSString stringWithFormat:@"%.0f",h]];
    
    
    html=[html stringByReplacingOccurrencesOfString:@"$top" withString:[NSString stringWithFormat:@"%.0f",top]];
    

    html=[html stringByReplacingOccurrencesOfString:@"$bottom" withString:[NSString stringWithFormat:@"%.0f",bottom]];
    
    NSLog(@"%@",html);
    NSLog(@"%@",[NSString stringWithFormat:@"%@/web/",[[NSBundle mainBundle] bundlePath]]);
    [self.webLineChartView loadHTMLString:html baseURL:[NSURL fileURLWithPath:[NSString stringWithFormat:@"%@/web/",[[NSBundle mainBundle] bundlePath]]]];
}

-(void)changePeriod:(id)sender{
    UIButton* button = (UIButton*)sender;
    [self.dailyButton setImage:[UIImage imageNamed:@"day"] forState:UIControlStateNormal];
    [self.weeklyButton setImage:[UIImage imageNamed:@"week"] forState:UIControlStateNormal];
    [self.monthlyButton setImage:[UIImage imageNamed:@"month"] forState:UIControlStateNormal];
    [self.yearlyButton setImage:[UIImage imageNamed:@"year"] forState:UIControlStateNormal];
    if(button==self.dailyButton){
        self.lineChart=self.dailyLineChart;
        self.periodImageView.image = [UIImage imageNamed:@"day_arrow"];
        [self.dailyButton setImage:[UIImage imageNamed:@"day_01"] forState:UIControlStateNormal];
    }else if(button==self.weeklyButton){
        self.lineChart=self.weeklyLineChart;
        self.periodImageView.image = [UIImage imageNamed:@"week_arrow"];
        [self.weeklyButton setImage:[UIImage imageNamed:@"week_01"] forState:UIControlStateNormal];
        
    }else if(button==self.monthlyButton){
        self.lineChart=self.monthlyLineChart;
        self.periodImageView.image = [UIImage imageNamed:@"month_arrow"];
        [self.monthlyButton setImage:[UIImage imageNamed:@"month_01"] forState:UIControlStateNormal];
    }else if(button==self.yearlyButton){
        self.lineChart=self.yearlyLineChart;
        self.periodImageView.image = [UIImage imageNamed:@"year_arrow"];
        [self.yearlyButton setImage:[UIImage imageNamed:@"year_01"] forState:UIControlStateNormal];
    }
    if(self.lineChart!=nil&&![self.lineChart isEqualToString:@"[\n\n]"]){
        [self generateInfoView:self.lineChart index:-1];
        [self generateLikeChart:self.lineChart];
    }else{
        TTAlert(@"没有相关图标数据");
    }
    
}
- (void)webViewDidFinishLoad:(UIWebView *)webView{
    #ifdef __IPHONE_7_0
    JSContext* context = [webView valueForKeyPath:@"documentView.webView.mainFrame.javaScriptContext"];
    context[@"nativeClickGraphItem"]=^(int index){
        [self nativeClickGraphItem:index];
    };
    #endif
}

-(void) nativeClickGraphItem:(int) index{
    NSLog(@"%i",index);
    [self generateInfoView:self.lineChart index:index];
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
    TT_RELEASE_SAFELY(_webLineChartView);
    
    TT_RELEASE_SAFELY(_dailyLineChart);
    TT_RELEASE_SAFELY(_weeklyLineChart);
    TT_RELEASE_SAFELY(_monthlyLineChart);
    TT_RELEASE_SAFELY(_yearlyLineChart);
    
    TT_RELEASE_SAFELY(_dailyButton);
    TT_RELEASE_SAFELY(_weeklyButton);
    TT_RELEASE_SAFELY(_monthlyButton);
    TT_RELEASE_SAFELY(_yearlyButton);
    TT_RELEASE_SAFELY(_infoView);
    [super dealloc];
}
@end
