//
//  StoreSumController.m
//  dailyreport
//
//  Created by zhang wei on 14-11-24.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "StoreSumController.h"
#import "StyleSheet.h"
#import "AppDelegate.h"
@implementation StoreSumController
@synthesize storeSumModel = _storeSumModel;
@synthesize typeLabels = _typeLabels;
@synthesize tableViews = _tableViews;
@synthesize monthlySums = _monthlySums;
@synthesize yearlySums = _yearlySums;
-(id) initWithBrand:(NSString*) brand{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.brand = brand;
        self.storeSumModel = [[[StoreSumModel alloc] initWithBrand:self.brand delegate:self] autorelease];
        self.index = 7;
        self.typeLabels = [NSMutableArray array];
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
    [self.storeSumModel load:TTURLRequestCachePolicyNone more:NO];
}

-(void)changeDateAndReload{
    [super changeDateAndReload];
    [self.storeSumModel load:TTURLRequestCachePolicyNone more:NO];
}

- (void) brandDidFinishLoad:(NSArray*) monthlySums yearlySums:(NSArray *)yearlySums date:(NSDate *)date {
    NSLog(@"%@",@"加载Store sum数据成功");
    self.selectedDate = date;
    self.monthlySums = monthlySums;
    self.yearlySums = yearlySums;
    UIView* dailyView = [self createDailyView:@"图标-拓展统计" label:@"拓展统计-数量"];
    
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
    
    NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setTimeStyle:NSDateFormatterFullStyle];
    [dateFormatter setDateFormat:@"MM月"];
    
    NSMutableArray* types = [NSMutableArray array];
    [types addObject:[dateFormatter stringFromDate:date]];
    [types addObject:@"本年"];
    if([types count]!=0){
        CGFloat width = SCREEN_WIDTH/2;
        int index = 0;
        CGFloat t = 0;
        for(NSString* type in types){
            CGFloat x = index*width;
            CGFloat realWidth = width-0.5;
            if(index!=0){
                x =x +0.5;
            }
            if(index==[types count]-1){
                realWidth = SCREEN_WIDTH-t;
            }
            t+=width;
            UILabel* typeLabel = [self createLabel:type frame:CGRectMake(x, dailyView.frame.size.height, realWidth, SCREEN_HEIGHT*4.6/48) textColor:@"#636363" font:channelFontSize backgroundColor:@"#ffffff"];
            UITapGestureRecognizer* recognizer = [[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickTypeLabel:)] autorelease];
            typeLabel.userInteractionEnabled = YES;
            [typeLabel addGestureRecognizer:recognizer];
            typeLabel.textAlignment = [self getAlignment:ALIGN_CENTER];
            if(index!=0){
                typeLabel.textColor = [UIColor whiteColor];
                typeLabel.backgroundColor = [StyleSheet colorFromHexString:TAB_COLOR];
            }
            [self.contentView addSubview:typeLabel];
            [self.typeLabels addObject:typeLabel];
            index++;
        }
    }
    
    CGFloat wOffset = 0;
    CGFloat hOffset = SCREEN_HEIGHT*6/48+dailyView.frame.size.height;
    [self.contentView addSubview:[self createLabel:@"" frame:CGRectMake(0, hOffset, SCREEN_WIDTH*4/14, SCREEN_HEIGHT*30/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    wOffset+=SCREEN_WIDTH*4/14+2;
    [self.contentView addSubview:[self createLabel:@"合计" frame:CGRectMake(wOffset, hOffset, SCREEN_WIDTH*2/14, SCREEN_HEIGHT*30/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    wOffset+=SCREEN_WIDTH*2/14+2;
    [self.contentView addSubview:[self createLabel:@"直营" frame:CGRectMake(wOffset, hOffset, SCREEN_WIDTH*2/14, SCREEN_HEIGHT*30/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    wOffset+=SCREEN_WIDTH*2/14+2;
    [self.contentView addSubview:[self createLabel:@"加盟" frame:CGRectMake(wOffset, hOffset, SCREEN_WIDTH*2/14, SCREEN_HEIGHT*30/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    wOffset+=SCREEN_WIDTH*2/14+2;
    
    [self.contentView addSubview:[self createLabel:@"联营分销" frame:CGRectMake(wOffset, hOffset,SCREEN_WIDTH-wOffset, SCREEN_HEIGHT*30/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    hOffset += SCREEN_HEIGHT*30/480;
    _tableOffset = hOffset;
    
    [self updateType:[dateFormatter stringFromDate:date]];

}


-(void)clickTypeLabel:(UIGestureRecognizer*)gestureRecognizer{
    UILabel* typeLabel = (UILabel*)gestureRecognizer.view;
    for(UILabel* l in self.typeLabels){
        l.textColor = [UIColor whiteColor];
        l.backgroundColor = [StyleSheet colorFromHexString:TAB_COLOR];
    }
    typeLabel.textColor = [StyleSheet colorFromHexString:@"#636363"];
    typeLabel.backgroundColor =[StyleSheet colorFromHexString:@"#ffffff"];
    
    
    [self updateType:typeLabel.text];
}

-(void) updateType:(NSString*) type{
    for(UIView* view in self.tableViews){
        [view removeFromSuperview];
    }
    CGFloat ROW_HEIGHT = 34;
    CGFloat ROW_CONTENT_HEIGHT=32;
    int labelFont = 12;
    if (SCREEN_WIDTH==320){
        ROW_HEIGHT =34;
        ROW_CONTENT_HEIGHT =32;
        labelFont = 12;
    }else if (SCREEN_WIDTH==375){
        ROW_HEIGHT= 38;
        ROW_CONTENT_HEIGHT= 36;
        labelFont = 13;
    }else{
        ROW_HEIGHT =40;
        ROW_CONTENT_HEIGHT =38;
        labelFont = 14;
    }
    
    
    [self.tableViews removeAllObjects];
    NSArray* storeSums = nil;
    if([type isEqualToString:@"本年"]){
        storeSums = self.yearlySums;
    }else{
        storeSums = self.monthlySums;
    }
    for(int i=0;i<storeSums.count;i++){
        CGFloat wOffset = 0;
        StoreSum* s = [storeSums objectAtIndex:i];
        UILabel* label =[self createLabel:s.sumType frame:CGRectMake(0, _tableOffset+i*ROW_HEIGHT, SCREEN_WIDTH*4/14, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:labelFont backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        wOffset+=SCREEN_WIDTH*4/14+2;
        [self.tableViews addObject:label];
        [self.contentView addSubview:label];
        label = [self createDecimalLabel:s.total frame:CGRectMake(wOffset, _tableOffset+i*ROW_HEIGHT, SCREEN_WIDTH*2/14, ROW_CONTENT_HEIGHT)  textColor:@"#6a6a6a" font:labelFont backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        wOffset+=SCREEN_WIDTH*2/14+2;
        [self.contentView addSubview:label];
        [self.tableViews addObject:label];
        
        label = [self createDecimalLabel:s.selfV frame:CGRectMake(wOffset, _tableOffset+i*ROW_HEIGHT, SCREEN_WIDTH*2/14, ROW_CONTENT_HEIGHT)  textColor:@"#6a6a6a" font:labelFont backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        wOffset+=SCREEN_WIDTH*2/14+2;
        [self.contentView addSubview:label];
        [self.tableViews addObject:label];
        
        label = [self createDecimalLabel:s.join frame:CGRectMake(wOffset, _tableOffset+i*ROW_HEIGHT, SCREEN_WIDTH*2/14, ROW_CONTENT_HEIGHT)  textColor:@"#6a6a6a" font:labelFont backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        wOffset+=SCREEN_WIDTH*2/14+2;
        [self.contentView addSubview:label];
        [self.tableViews addObject:label];
        
        label =[self createDecimalLabel:s.unionV frame:CGRectMake(wOffset, _tableOffset+i*ROW_HEIGHT,SCREEN_WIDTH-wOffset, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:labelFont backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        [self.contentView addSubview:label];
        [self.tableViews addObject:label];
    }
    
    self.contentView.contentSize =
    CGSizeMake(SCREEN_WIDTH, _tableOffset+storeSums.count*ROW_HEIGHT);
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
    _storeSumModel.delegate = nil;
    TT_RELEASE_SAFELY(_storeSumModel);
    TT_RELEASE_SAFELY(_typeLabels);
    TT_RELEASE_SAFELY(_tableViews);
    TT_RELEASE_SAFELY(_monthlySums);
    TT_RELEASE_SAFELY(_monthlySums);
    
    [super dealloc];
}
@end
