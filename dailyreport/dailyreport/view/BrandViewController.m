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

@implementation BrandViewController


@synthesize brandModel = _brandModel;
@synthesize webLineChartView = _webLineChartView;

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

- (void) brandDidFinishLoad:(Brand*) brand channels:(NSArray*) channels dailyLineChart:(NSString*)dailyLineChart{
     NSLog(@"%@",@"加载Brand数据成功");
    self.selectedDate = brand.date;
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
    
    
    self.webLineChartView = [[[UIWebView alloc] initWithFrame:CGRectMake(0,dailyView.frame.size.height,SCREEN_WIDTH,SCREEN_HEIGHT-dailyView.frame.size.height)] autorelease];
    self.webLineChartView.scalesPageToFit=YES;
    self.webLineChartView.userInteractionEnabled =YES;
    CannotCancelUISwipeGestureRecognizer* swipeGestureLeft = [[[CannotCancelUISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(handleGesture:)] autorelease];
    swipeGestureLeft.direction = UISwipeGestureRecognizerDirectionLeft;
    [self.webLineChartView.scrollView addGestureRecognizer:swipeGestureLeft];


    [self.contentView addSubview:self.webLineChartView];
    NSString* htmlPath = [[NSBundle mainBundle] pathForResource:@"dailyLineChart" ofType:@"html" inDirectory:@"web"];
    NSString* html = [NSString stringWithContentsOfFile:htmlPath encoding:NSUTF8StringEncoding error:nil];
    
    html=[html stringByReplacingOccurrencesOfString:@"$dataProvider" withString:dailyLineChart];
    

    NSLog(@"%@",html);
    NSLog(@"%@",[NSString stringWithFormat:@"%@/web/",[[NSBundle mainBundle] bundlePath]]);
    [self.webLineChartView loadHTMLString:html baseURL:[NSURL fileURLWithPath:[NSString stringWithFormat:@"%@/web/",[[NSBundle mainBundle] bundlePath]]]];
   
    
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
    TT_RELEASE_SAFELY(_webLineChartView);
    [super dealloc];
}
@end
