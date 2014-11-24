//
//  NoRetailsController.m
//  dailyreport
//
//  Created by zhang wei on 14-11-24.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "NoRetailsController.h"
#import "StyleSheet.h"
#import "AppDelegate.h"
@implementation NoRetailsController
@synthesize noRetailModel = _noRetailModel;

-(id) initWithBrand:(NSString*) brand{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.brand = brand;
        self.noRetailModel = [[[NoRetailModel alloc] initWithBrand:self.brand delegate:self] autorelease];
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
    [self.noRetailModel load:TTURLRequestCachePolicyNone more:NO];
}

-(void)changeDateAndReload{
    [super changeDateAndReload];
    [self.noRetailModel load:TTURLRequestCachePolicyNone more:NO];
}

- (void) brandDidFinishLoad:(NSArray*) nrs date:(NSDate *)date {
    NSLog(@"%@",@"加载No Retail数据成功");
    self.selectedDate = date;
    
    UIView* dailyView = [self createDailyView:@"图标-未上传店铺" label:@"未上传店铺"];
    
    [self.contentView addSubview:dailyView];
    int channelFontSize = 20;
    int labelFontSize =14;
    NSLog(@"%f",SCREEN_WIDTH);
    if(SCREEN_WIDTH==320){
        channelFontSize = 20;
        labelFontSize =16;
    }else if(SCREEN_WIDTH==375){
        channelFontSize = 22;
        labelFontSize =18;
    }else{
        channelFontSize = 24;
        labelFontSize =20;
    }
    

    


    
    UILabel* typeLabel = [self createLabel:@"    店铺名称" frame:CGRectMake(0, dailyView.frame.size.height, SCREEN_WIDTH, SCREEN_HEIGHT*5/48) textColor:@"#ffffff" font:channelFontSize backgroundColor:TAB_COLOR];
   
    typeLabel.textAlignment = [self getAlignment:ALIGN_LEFT];
        [self.contentView addSubview:typeLabel];
    
    _tableOffset = SCREEN_HEIGHT*5/48+dailyView.frame.size.height;;
    
    
    CGFloat ROW_HEIGHT = 54;
    CGFloat ROW_CONTENT_HEIGHT=52;
    int labelFont = 20;
    if (SCREEN_WIDTH==320){
        ROW_HEIGHT =54;
        ROW_CONTENT_HEIGHT =52;
        labelFont = 20;
    }else if (SCREEN_WIDTH==375){
        ROW_HEIGHT= 58;
        ROW_CONTENT_HEIGHT= 56;
        labelFont = 21;
    }else{
        ROW_HEIGHT =60;
        ROW_CONTENT_HEIGHT =58;
        labelFont = 22;
    }
    
    
    for(int i=0;i<nrs.count;i++){
        NSString* storeName = [nrs objectAtIndex:i];
        UILabel* label =[self createLabel:[NSString stringWithFormat:@"    %@",storeName] frame:CGRectMake(0, _tableOffset+i*ROW_HEIGHT, SCREEN_WIDTH, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:labelFont backgroundColor:@"#f3f3f3" textAlignment:ALIGN_LEFT];
        [self.contentView addSubview:label];
    }
    
    self.contentView.contentSize =
    CGSizeMake(SCREEN_WIDTH, _tableOffset+nrs.count*ROW_HEIGHT);
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
    _noRetailModel.delegate = nil;
    TT_RELEASE_SAFELY(_noRetailModel);
    
    [super dealloc];
}
@end
