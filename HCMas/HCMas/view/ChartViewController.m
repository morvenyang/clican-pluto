//
//  ChartViewController.m
//  HCMas
//
//  Created by zhang wei on 14-11-4.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import "ChartViewController.h"
#import "AppDelegate.h"
#import "Constants.h"
@implementation ChartViewController
@synthesize kpiType = _kpiType;
@synthesize pointName = _pointName;
@synthesize startTime = _startTime;
@synthesize endTime = _endTime;
@synthesize webPieChartView = _webPieChartView;
-(id) initWithKpiType:(NSString*)kpiType pointName:(NSString*)pointName startTime:(NSString*)startTime endTime:(NSString*)endTime{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.kpiType = kpiType;
        self.pointName = pointName;
        self.startTime = startTime;
        self.endTime = endTime;
        _kpiHistoryModel = [[KpiHistoryModel alloc] init];
        _kpiHistoryModel.delegate = self;
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
-(void)loadView{
    [super loadView];

    NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:@"yyyyMMdd"];
    [_kpiHistoryModel loadHistoryKpiByProjectId:HCMasAppDelegate.user.selectedProject.projectId kpiType:self.kpiType pointName:self.pointName startDate:[dateFormatter dateFromString:self.startTime] endDate:[dateFormatter dateFromString:self.endTime]];
    
}
-(void)viewWillAppear:(BOOL)animated{
    self.navigationController.navigationBarHidden = NO;
    [[UIApplication sharedApplication] setStatusBarOrientation:UIInterfaceOrientationLandscapeLeft];
    [[UIApplication sharedApplication] setStatusBarHidden:YES];
    //计算旋转角度
    float arch=-M_PI_2;
    
    
    //对navigationController.view 进行强制旋转
    self.navigationController.view.transform = CGAffineTransformMakeRotation(arch);
    self.navigationController.view.bounds = CGRectMake(0, 0, SCREEN_HEIGHT, SCREEN_WIDTH);

}
-(void)viewWillDisappear:(BOOL)animated{
    self.navigationController.navigationBarHidden = YES;

    [[UIApplication sharedApplication] setStatusBarHidden:NO];
        //计算旋转角度
        float arch=0;
        
        
        //对navigationController.view 进行强制旋转
        self.navigationController.view.transform = CGAffineTransformMakeRotation(arch);
        self.navigationController.view.bounds = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

    
}
-(NSString*)getUnitForKpiType:(NSString*)kt{
    if([kt isEqualToString:@"Reservoir"]){
        return @"m";
    }else if([kt  isEqualToString:@"Saturation"]){
        return @"m";
    }else if([kt isEqualToString:@"Rainfall"]){
        return @"mm";
    }else if([kt isEqualToString:@"SeeFlow"]){
        return @"m/s";
    }else if([kt isEqualToString:@"DryBeach"]){
        return @"m";
    }else if([kt isEqualToString:@"Tyl"]){
        return @"Mpa";
    }else if([kt isEqualToString:@"Rxwy"]){
        return @"m";
    }else if([kt isEqualToString:@"Lf"]){
        return @"m";
    }else if([kt isEqualToString:@"Wd"]){
        return @"℃";
    }else if([kt isEqualToString:@"Thsl"]){
        return @"%";
    }else if([kt isEqualToString:@"Dqwd"]){
        return @"℃";
    }else if([kt isEqualToString:@"Dqsd"]){
        return @"%";
    }else if([kt isEqualToString:@"Dqyl"]){
        return @"Mpa";
    }else if([kt isEqualToString:@"Fx"]){
        return @"°";
    }else if([kt isEqualToString:@"Fs"]){
        return @"m/s";
    }else{
        return @"";
    }
}
#pragma mark -
#pragma mark KpiHistoryDelegate methods
- (void)loadKpiHistoryStart{

}
- (void)loadKpiHistorySuccess:(NSArray*) kpis{
    long width = 0;
    NSString* fileName = @"historyLineOther";
    if([self.kpiType isEqualToString:@"Surface"]){
        fileName= @"historyLineSurface";
        width = 300+kpis.count*50;
    }else if([self.kpiType isEqualToString:@"Inner"]){
        fileName= @"historyLineInner";
        width = 200+kpis.count*50;
    }else{
        width = 100+kpis.count*50;
    }
    if(width<1500){
        width = 1500;
    }
    NSLog(@"%f,%f",SCREEN_WIDTH,SCREEN_HEIGHT);
    long height = SCREEN_WIDTH;
    UIScrollView* scrollView = [[[UIScrollView alloc] initWithFrame:CGRectMake(0,0,SCREEN_HEIGHT,height)] autorelease];
    if(self.webPieChartView){
        [self.webPieChartView removeFromSuperview];
    }
    self.webPieChartView = [[[UIWebView alloc] initWithFrame:CGRectMake(0,0,width/2,height)] autorelease];
    
    self.webPieChartView.scrollView.scrollEnabled=NO;
    NSString* dataProvider = [[[NSString alloc] initWithData:[NSJSONSerialization dataWithJSONObject:kpis options:NSJSONWritingPrettyPrinted error:nil] encoding:NSUTF8StringEncoding] autorelease];
    
    NSString* htmlPath = [[NSBundle mainBundle] pathForResource:fileName ofType:@"html" inDirectory:@"web"];
    NSString* html = [NSString stringWithContentsOfFile:htmlPath encoding:NSUTF8StringEncoding error:nil];
    
    html=[html stringByReplacingOccurrencesOfString:@"$dataProvider" withString:dataProvider];
    html=[html stringByReplacingOccurrencesOfString:@"$width" withString:[NSString stringWithFormat:@"%li",width]];
    html=[html stringByReplacingOccurrencesOfString:@"$height" withString:[NSString stringWithFormat:@"%li",height*2]];
    html=[html stringByReplacingOccurrencesOfString:@"$unit" withString:[self getUnitForKpiType:self.kpiType]];

    NSLog(@"%@",html);
    NSLog(@"%@",[NSString stringWithFormat:@"%@/web/",[[NSBundle mainBundle] bundlePath]]);
    [self.webPieChartView loadHTMLString:html baseURL:[NSURL fileURLWithPath:[NSString stringWithFormat:@"%@/web/",[[NSBundle mainBundle] bundlePath]]]];
    
    [scrollView addSubview:self.webPieChartView];
    [self.view addSubview:scrollView];
    scrollView.contentSize = CGSizeMake(width/2,height);
    self.webPieChartView.scalesPageToFit=YES;
    self.webPieChartView.userInteractionEnabled =YES;
}
- (void)loadKpiHistoryFailed:(NSError*) error message:(NSString*) message{

    //-1004 connection is not available
    //-1001 timeout
    if([error code]==-1004||[error code]==-1001){
        TTAlert(@"请检查网络链接");
    }else{
        if(message){
            TTAlert(message);
        }else{
            TTAlert([error localizedDescription]);
        }
    }
}
//-(NSUInteger)supportedInterfaceOrientations{
//    return UIInterfaceOrientationMaskLandscapeLeft;
//}
//-(UIInterfaceOrientation)preferredInterfaceOrientationForPresentation{
//    return UIInterfaceOrientationLandscapeLeft;
//}
//-(BOOL)shouldAutorotate{
//    return YES;
//}
- (void)dealloc
{
    TT_RELEASE_SAFELY(_pointName);
    TT_RELEASE_SAFELY(_kpiType);
    TT_RELEASE_SAFELY(_startTime);
    TT_RELEASE_SAFELY(_endTime);
    TT_RELEASE_SAFELY(_webPieChartView);
    _kpiHistoryModel.delegate = nil;
    TT_RELEASE_SAFELY(_kpiHistoryModel);
    [super dealloc];
}
@end
