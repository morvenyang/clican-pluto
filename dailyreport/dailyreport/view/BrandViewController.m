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
#import <JavaScriptCore/JavaScriptCore.h>

@implementation BrandViewController


@synthesize brandModel = _brandModel;
@synthesize webLineChartView = _webLineChartView;

@synthesize dailyLineChart = _dailyLineChart;
@synthesize weeklyLineChart = _weeklyLineChart;
@synthesize monthlyLineChart = _monthlyLineChart;
@synthesize yearlyLineChart = _yearlyLineChart;

@synthesize dailyButton = _dailyButton;
@synthesize weeklyButton = _weeklyButton;
@synthesize monthlyButton = _monthlyButton;
@synthesize yearlyButton = _yearlyButton;
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
    
    int labelFontSize = 12;
    int channelFontSize = 14;
    NSLog(@"%f",SCREEN_WIDTH);
    if(SCREEN_WIDTH==320){
        labelFontSize = 12;
        channelFontSize = 14;
    }else if(SCREEN_WIDTH==375){
        labelFontSize = 14;
        channelFontSize = 16;
    }else{
        labelFontSize = 16;
        channelFontSize = 18;
    }

    UIView* dailyView = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH,SCREEN_HEIGHT/3)] autorelease];
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
    
    
    
    if(SCREEN_WIDTH==320){
        if(retailAmountLabel.text.length>5){
            retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:50];
        }else if(retailAmountLabel.text.length>4){
            retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:60];
        }else{
            retailAmountLabel.font = [UIFont fontWithName:@"HelveticaNeue-UltraLight" size:70];
        }
    }else if(SCREEN_WIDTH==350){
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
    UILabel* retailLikeLabel = [self createLabel:retailLikeText frame:CGRectMake(40, 130, SCREEN_WIDTH/2-40, 30) textColor:@"#ffffff" font:20 backgroundColor:nil];
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
    
    
    UIImage* dayImage = [UIImage imageNamed:@"天"];
    CGFloat space = (SCREEN_WIDTH-dayImage.size.width*4)/12;
    CGFloat wOffset = space;
    CGFloat periodHeight = dayImage.size.height*3;
    CGFloat yOffset = dailyView.frame.size.height;
    self.dailyButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.dailyButton setImage:[UIImage imageNamed:@"天"] forState:UIControlStateNormal];
    [self.dailyButton addTarget:self action:@selector(changePeriod:) forControlEvents:UIControlEventTouchUpInside];
    self.dailyButton.frame = CGRectMake(wOffset, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height);
    [self.contentView addSubview:self.dailyButton];
    [self.contentView addSubview:[self createLabel:@"天" frame:CGRectMake(wOffset+dayImage.size.width+3, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height) textColor:@"#000000" font:labelFontSize backgroundColor:nil]];
    
    wOffset+=space*3+dayImage.size.width;
    self.weeklyButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.weeklyButton setImage:[UIImage imageNamed:@"天"] forState:UIControlStateNormal];
    [self.weeklyButton addTarget:self action:@selector(changePeriod:) forControlEvents:UIControlEventTouchUpInside];
    self.weeklyButton.frame = CGRectMake(wOffset, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height);
    [self.contentView addSubview:self.weeklyButton];
    [self.contentView addSubview:[self createLabel:@"周" frame:CGRectMake(wOffset+dayImage.size.width+3, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height) textColor:@"#000000" font:labelFontSize backgroundColor:nil]];

    
    wOffset+=space*3+dayImage.size.width;
    self.monthlyButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.monthlyButton setImage:[UIImage imageNamed:@"天"] forState:UIControlStateNormal];
    [self.monthlyButton addTarget:self action:@selector(changePeriod:) forControlEvents:UIControlEventTouchUpInside];
    self.monthlyButton.frame = CGRectMake(wOffset, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height);
    [self.contentView addSubview:self.monthlyButton];
    [self.contentView addSubview:[self createLabel:@"月" frame:CGRectMake(wOffset+dayImage.size.width+3, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height) textColor:@"#000000" font:labelFontSize backgroundColor:nil]];
    
    wOffset+=space*3+dayImage.size.width;
    self.yearlyButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [self.yearlyButton setImage:[UIImage imageNamed:@"天"] forState:UIControlStateNormal];
    [self.yearlyButton addTarget:self action:@selector(changePeriod:) forControlEvents:UIControlEventTouchUpInside];
    self.yearlyButton.frame = CGRectMake(wOffset, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height);
    [self.contentView addSubview:self.yearlyButton];
    [self.contentView addSubview:[self createLabel:@"年" frame:CGRectMake(wOffset+dayImage.size.width+3, yOffset+periodHeight/6, dayImage.size.width, dayImage.size.height) textColor:@"#000000" font:labelFontSize backgroundColor:nil]];
    yOffset+=periodHeight;
    self.webLineChartView = [[[UIWebView alloc] initWithFrame:CGRectMake(0,yOffset,SCREEN_WIDTH,SCREEN_HEIGHT-yOffset)] autorelease];
    self.webLineChartView.scalesPageToFit=YES;
    self.webLineChartView.userInteractionEnabled =YES;
    self.webLineChartView.delegate = self;
    CannotCancelUISwipeGestureRecognizer* swipeGestureLeft = [[[CannotCancelUISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleGesture:)] autorelease];
    swipeGestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    [self.webLineChartView.scrollView addGestureRecognizer:swipeGestureLeft];

    [self.contentView addSubview:self.webLineChartView];
    [self generateLikeChart:self.dailyLineChart];

    
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
-(void)generateLikeChart:(NSString*)dataProvider{
    NSString* htmlPath = [[NSBundle mainBundle] pathForResource:@"dailyLineChart" ofType:@"html" inDirectory:@"web"];
    NSString* html = [NSString stringWithContentsOfFile:htmlPath encoding:NSUTF8StringEncoding error:nil];
    
    html=[html stringByReplacingOccurrencesOfString:@"$dataProvider" withString:dataProvider];
    html=[html stringByReplacingOccurrencesOfString:@"$width" withString:@"1000"];
    
    html=[html stringByReplacingOccurrencesOfString:@"$height" withString:@"600"];
    
    NSLog(@"%@",html);
    NSLog(@"%@",[NSString stringWithFormat:@"%@/web/",[[NSBundle mainBundle] bundlePath]]);
    [self.webLineChartView loadHTMLString:html baseURL:[NSURL fileURLWithPath:[NSString stringWithFormat:@"%@/web/",[[NSBundle mainBundle] bundlePath]]]];
}

-(void)changePeriod:(id)sender{
    UIButton* button = (UIButton*)sender;
    if(button==self.dailyButton){
        [self generateLikeChart:self.dailyLineChart];
    }else if(button==self.weeklyButton){
        [self generateLikeChart:self.weeklyLineChart];
    }else if(button==self.monthlyButton){
        [self generateLikeChart:self.monthlyLineChart];
    }else if(button==self.yearlyButton){
        [self generateLikeChart:self.yearlyLineChart];
    }
}
- (void)webViewDidFinishLoad:(UIWebView *)webView{
    JSContext* context = [webView valueForKeyPath:@"documentView.webView.mainFrame.javaScriptContext"];
    context[@"nativeClickGraphItem"]=^(NSString* date){
        [self nativeClickGraphItem:date];
    };
    
}

-(void) nativeClickGraphItem:(NSString*) date{
    NSLog(@"%@",date);
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
    [super dealloc];
}
@end
