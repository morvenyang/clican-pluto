//
//  GoodViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-7-30.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "GoodViewController.h"
#import "AppDelegate.h"
#import "GoodRank.h"
#import "CRNavigator.h"

@implementation GoodViewController
@synthesize dyviews = _dyviews;
@synthesize pointImageViews = _pointImageViews;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(id) initWithBrand:(NSString*) brand index:(int)index{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.brand = brand;
        self.index=-1;
        self.dyviews = [NSMutableArray array];
        self.pointImageViews =[NSMutableArray array];
    }
    return self;
}

-(void) backAction{
    NSString* url = [NSString  stringWithFormat:@"peacebird://goodRank/%@", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    TTOpenURL(url);
}

- (void)loadView
{
    [super loadView];
    self.contentView.goodSwitchDelegate = self;
    UIView* dailyView = [self createDailyView:@"图标-零售收入" label:@"商品信息"];
    
    
    
    [self.contentView addSubview:dailyView];
    _footOffset =dailyView.frame.size.height+10;
    [self switchGood];
}
-(NSString*) getScaleUrl:(NSString*)url{
    url = [url stringByReplacingOccurrencesOfString:[[url lastPathComponent] stringByAppendingString:@"/"] withString:@""];
    url = [url stringByReplacingOccurrencesOfString:[[url lastPathComponent] stringByAppendingString:@"/"] withString:@""];
    NSString* result = [url stringByAppendingString:@"440/640/"];
    NSLog(@"%@",result);
    return result;
}
-(void)switchGood{
    for(UIView* view in self.dyviews){
        [view removeFromSuperview];
    }
    int i = 0;
    UIImage* lightImage= [UIImage imageNamed:@"图标-分页原点-正常.png"];
    UIImage* highLightImage= [UIImage imageNamed:@"图标-分页原点-高亮.png"];
    for(UIImageView* view in self.pointImageViews){
        if(i==DrAppDelegate.user.goodIndex){
            view.image =highLightImage;
        }else{
            view.image =lightImage;
        }
        i++;
        
    }
    GoodRank* gr = [DrAppDelegate.user.goods objectAtIndex:DrAppDelegate.user.goodIndex];
    CGFloat ratio = 16.0/11;
    if(IS_IPHONE4){
        ratio = 16.0/12;
    }
    TTImageView* imageView = [[[TTImageView alloc] initWithFrame:CGRectMake(10,_footOffset , SCREEN_WIDTH-20, (SCREEN_WIDTH-20)*ratio)] autorelease];
    imageView.urlPath = gr.imageLink;
    NSLog(@"%f %f",imageView.frame.size.width,imageView.frame.size.height);
    [self.contentView addSubview:imageView];
    [self.dyviews addObject:imageView];
    
    
    

    UIView* footView =[self createFootView:gr y:_footOffset+(SCREEN_WIDTH-20)*ratio-SCREEN_HEIGHT*100/480];
    [self.contentView addSubview:footView];
    [self.dyviews addObject:footView];
    self.contentView.contentSize =
    CGSizeMake(SCREEN_WIDTH, _footOffset+(SCREEN_WIDTH-20)*ratio);
    if(SCREEN_WIDTH!=320){
        self.contentView.scrollEnabled=NO;
    }
}

-(UIView*) createFootView:(GoodRank*) gr y:(CGFloat)y{
    
    
    UIView* footView = [[[UIView alloc] initWithFrame:CGRectMake(10, y, SCREEN_WIDTH-20, SCREEN_HEIGHT*100/480)] autorelease];
    NSLog(@"%f",self.contentView.frame.size.height);
    NSLog(@"%f",footView.frame.size.height);
    footView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.3f];
    UIView* view = nil;
    int labelFontSize =16;
    int noLabelFontSize = 25;
    NSLog(@"%f",SCREEN_WIDTH);
    if(SCREEN_WIDTH==320){
        labelFontSize =16;
        noLabelFontSize = 25;
    }else if(SCREEN_WIDTH==375){
        labelFontSize =18;
        noLabelFontSize = 28;
    }else{
        labelFontSize =20;
        noLabelFontSize = 31;
    }
    CGFloat hOffset = 0;
    view =[self createLabel:[NSString stringWithFormat:@"No. %i",DrAppDelegate.user.goodIndex+1] frame:CGRectMake(10, 0, SCREEN_WIDTH*100/320, SCREEN_HEIGHT*30/480) textColor:@"#ff6501" font:noLabelFontSize backgroundColor:nil];
    [footView addSubview:view];
    hOffset+=SCREEN_HEIGHT*30/480;
    view =[self createLabel:[NSString stringWithFormat:@"品名: %@",gr.name] frame:CGRectMake(10, hOffset, SCREEN_WIDTH*200/320, SCREEN_HEIGHT*20/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:nil];
    [footView addSubview:view];
    

    
    view =[self createLabel:@"件数:" frame:CGRectMake(SCREEN_WIDTH*210/320, hOffset, SCREEN_WIDTH*40/320, SCREEN_HEIGHT*20/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:nil];
    [footView addSubview:view];
    
    view =[self createDecimalLabel:[NSNumber numberWithInt:gr.count.intValue] frame:CGRectMake(SCREEN_WIDTH*251/320, hOffset, SCREEN_WIDTH*70/320, SCREEN_HEIGHT*20/480) textColor:@"#ff6501" font:labelFontSize backgroundColor:nil textAlignment:ALIGN_LEFT];
    [footView addSubview:view];
    hOffset+=SCREEN_HEIGHT*20/480;
    view =[self createLabel:[NSString stringWithFormat:@"系列: %@",gr.line] frame:CGRectMake(10, hOffset, SCREEN_WIDTH*200/320, SCREEN_HEIGHT*20/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:nil];
    [footView addSubview:view];
    
    view =[self createLabel:[NSString stringWithFormat:@"年份: %@",gr.year] frame:CGRectMake(SCREEN_WIDTH*210/320, hOffset, SCREEN_WIDTH*90/320, SCREEN_HEIGHT*20/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:nil];
    [footView addSubview:view];
    

    hOffset+=SCREEN_HEIGHT*20/480;
    view =[self createLabel:[NSString stringWithFormat:@"波段: %@",gr.wave] frame:CGRectMake(10, hOffset, SCREEN_WIDTH*200/320, 20) textColor:@"#ffffff" font:labelFontSize backgroundColor:nil];
    [footView addSubview:view];
    
    view =[self createLabel:[NSString stringWithFormat:@"季节: %@",gr.season] frame:CGRectMake(SCREEN_WIDTH*210/320, hOffset, SCREEN_WIDTH*90/320, 20) textColor:@"#ffffff" font:labelFontSize backgroundColor:nil];
    [footView addSubview:view];
    return footView;
}
-(UIView*) createPaginationView:(int)y{
    UIView* paginationView = [[[UIView alloc] initWithFrame:CGRectMake((SCREEN_WIDTH-16*DrAppDelegate.user.goods.count)/2, y, 16*DrAppDelegate.user.goods.count, 30)] autorelease];
    UIImage* lightImage= [UIImage imageNamed:@"图标-分页原点-正常.png"];
    UIImage* highLightImage= [UIImage imageNamed:@"图标-分页原点-高亮.png"];
    paginationView.backgroundColor = [UIColor whiteColor];
    for(int i=1;i<=DrAppDelegate.user.goods.count;i++){
        UIImageView* v = [[[UIImageView alloc] initWithFrame:CGRectMake(16*(i-1), 10, 6, 6)] autorelease];
        if(i==DrAppDelegate.user.goodIndex){
            v.image = highLightImage;
        }else{
            v.image = lightImage;
        }
        [self.pointImageViews addObject:v];
        [paginationView addSubview:v];
    }
    return paginationView;
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

- (void)dealloc
{
    self.contentView.goodSwitchDelegate = nil;
    TT_RELEASE_SAFELY(_dyviews);
    TT_RELEASE_SAFELY(_pointImageViews);
    [super dealloc];
}

@end
