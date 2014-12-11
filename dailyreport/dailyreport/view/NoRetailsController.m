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
#import "NoRetails.h"
#import "Store.h"
@implementation NoRetailsController
@synthesize noRetailModel = _noRetailModel;
@synthesize channelLables = _channelLables;
@synthesize noRetails = _noRetails;
@synthesize tableViews = _tableViews;
-(id) initWithBrand:(NSString*) brand{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.brand = brand;
        self.noRetailModel = [[[NoRetailModel alloc] initWithBrand:self.brand delegate:self] autorelease];
        self.index = 7;
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
    self.noRetails = nrs;
    UIView* dailyView = [self createDailyView:@"图标-零售收入" label:@"未上传店铺"];
    
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
    

    CGFloat t = 0;
    if([nrs count]!=0){
        CGFloat width = SCREEN_WIDTH/[nrs count];
        int index = 0;
        for(NoRetails* noRetails in nrs){
            CGFloat x = index*width;
            CGFloat realWidth = width-0.5;
            if(index!=0){
                x =x +0.5;
            }
            if(index==[nrs count]-1){
                realWidth = SCREEN_WIDTH-t;
            }
            t+=width;
            UILabel* channelLabel = [self createLabel:noRetails.channel frame:CGRectMake(x, dailyView.frame.size.height, realWidth, [self getTabHeight]) textColor:@"#636363" font:channelFontSize backgroundColor:@"#ffffff"];
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
    

    CGFloat wOffset = 0;
    CGFloat hOffset = SCREEN_HEIGHT*2/48+dailyView.frame.size.height+[self getTabHeight];
    NoRetails* noRetails = [nrs objectAtIndex:0];
    [self.contentView addSubview:[self createLabel:@"店铺编号" frame:CGRectMake(0, hOffset, SCREEN_WIDTH*1/4, SCREEN_HEIGHT*30/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    wOffset+=SCREEN_WIDTH*1/4+2;
    [self.contentView addSubview:[self createLabel:[NSString stringWithFormat:@"店铺名称 %i家", noRetails.stores.count] frame:CGRectMake(wOffset, hOffset, SCREEN_WIDTH-wOffset, SCREEN_HEIGHT*30/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    hOffset += SCREEN_HEIGHT*30/480;
    _tableOffset = hOffset;
    [self updateChannel:noRetails];
}


-(void)clickChannelLabel:(UIGestureRecognizer*)gestureRecognizer{
    UILabel* channelLabel = (UILabel*)gestureRecognizer.view;
    
    for(UILabel* l in self.channelLables){
        l.textColor = [UIColor whiteColor];
        l.backgroundColor = [StyleSheet colorFromHexString:TAB_COLOR];
    }
    channelLabel.textColor = [StyleSheet colorFromHexString:@"#636363"];
    channelLabel.backgroundColor =[StyleSheet colorFromHexString:@"#ffffff"];
    for(NoRetails* nr in self.noRetails){
        if([nr.channel isEqualToString:channelLabel.text]){
             [self updateChannel:nr];
            break;
        }
    }
    
   
}

-(void) updateChannel:(NoRetails*) noRetails{
    for(UIView* view in self.tableViews){
        [view removeFromSuperview];
    }
    
    [self.tableViews removeAllObjects];
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
    
    for(int i=0;i<noRetails.stores.count;i++){
        Store* store = [noRetails.stores objectAtIndex:i];
        CGFloat wOffset=0;
        UILabel* label =[self createLabel:store.storeCode frame:CGRectMake(0, _tableOffset+i*ROW_HEIGHT, SCREEN_WIDTH*1/4, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:labelFont-3 backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        [self.tableViews addObject:label];
        [self.contentView addSubview:label];
        wOffset+=SCREEN_WIDTH*1/4+2;
        label =[self createLabel:store.storeName frame:CGRectMake(wOffset, _tableOffset+i*ROW_HEIGHT, SCREEN_WIDTH-wOffset, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:labelFont backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        [self.tableViews addObject:label];
        [self.contentView addSubview:label];
    }
    
    self.contentView.contentSize =
    CGSizeMake(SCREEN_WIDTH, _tableOffset+noRetails.stores.count*ROW_HEIGHT);
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
    TT_RELEASE_SAFELY(_channelLables);
    TT_RELEASE_SAFELY(_tableViews);
    TT_RELEASE_SAFELY(_noRetails);
    [super dealloc];
}
@end
