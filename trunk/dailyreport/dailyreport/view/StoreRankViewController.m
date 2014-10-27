//
//  StoreRankViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-5-26.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "StoreRankViewController.h"
#import "ChannelRank.h"
#import "Rank.h"
#import "StyleSheet.h"



@implementation StoreRankViewController

@synthesize storeRankModel = _storeRankModel;
@synthesize channelLables = _channelLables;
@synthesize channels = _channels;
@synthesize tableViews = _tableViews;
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(id) initWithBrand:(NSString*) brand{
    if ((self = [self initWithNibName:nil bundle:nil])) {
        self.brand = brand;
        self.storeRankModel = [[[StoreRankModel alloc] initWithBrand:self.brand delegate:self] autorelease];
        self.channelLables = [NSMutableArray array];
        self.tableViews =[NSMutableArray array];
        self.index = 4;
    }
    return self;
}

- (void)loadView
{
    [super loadView];
    [self.storeRankModel load:TTURLRequestCachePolicyNone more:NO];
}

-(void)changeDateAndReload{
    [super changeDateAndReload];
    [self.storeRankModel load:TTURLRequestCachePolicyNone more:NO];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void) brandDidFinishLoad:(NSMutableArray*) channels date:(NSDate*) date{
    NSLog(@"%@",@"加载Brand Store Rank数据成功");
    self.selectedDate = date;
    self.channels = channels;
    UIView* dailyView = [self createDailyView:@"图标-零售收入" label:@"店铺排名"];
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
    
    if([channels count]!=0){
        CGFloat width = SCREEN_WIDTH/[channels count];
        int index = 0;
        CGFloat t = 0;
        for(ChannelRank* channelRank in channels){
            CGFloat x = index*width;
            CGFloat realWidth = width-0.5;
            if(index!=0){
                x =x +0.5;
            }
            if(index==[channels count]-1){
                realWidth = SCREEN_WIDTH-t;
            }
            t+=width;
            UILabel* channelLabel = [self createLabel:channelRank.channel frame:CGRectMake(x, dailyView.frame.size.height, realWidth, SCREEN_HEIGHT*5/48) textColor:@"#636363" font:channelFontSize backgroundColor:@"#ffffff"];
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
    [self.contentView addSubview:dailyView];
    CGFloat wOffset = 0;
    CGFloat hOffset = SCREEN_HEIGHT*6/48+dailyView.frame.size.height;
    [self.contentView addSubview:[self createLabel:@"＃" frame:CGRectMake(0, hOffset, SCREEN_WIDTH*3/32, SCREEN_HEIGHT*40/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    wOffset+=SCREEN_WIDTH*3/32+2;
    [self.contentView addSubview:[self createLabel:@"店铺名称" frame:CGRectMake(wOffset, hOffset, SCREEN_WIDTH*186/320, SCREEN_HEIGHT*40/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    wOffset+=SCREEN_WIDTH*186/320+2;
    [self.contentView addSubview:[self createLabel:@"零售额" frame:CGRectMake(wOffset, hOffset, SCREEN_WIDTH*49/320, SCREEN_HEIGHT*40/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    wOffset+=SCREEN_WIDTH*49/320+2;
    [self.contentView addSubview:[self createLabel:@"占比" frame:CGRectMake(wOffset, hOffset,SCREEN_WIDTH-wOffset, SCREEN_HEIGHT*40/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    hOffset += SCREEN_HEIGHT*40/480;
    _tableOffset = hOffset;
    if(self.channels.count>0){
        [self updateChannel:[self.channels objectAtIndex:0]];
    }
    
}
-(void)clickChannelLabel:(UIGestureRecognizer*)gestureRecognizer{
    UILabel* channelLabel = (UILabel*)gestureRecognizer.view;
    ChannelRank* channel = nil;
    for(UILabel* l in self.channelLables){
        l.textColor = [UIColor whiteColor];
        l.backgroundColor = [StyleSheet colorFromHexString:TAB_COLOR];
    }
    channelLabel.textColor = [StyleSheet colorFromHexString:@"#636363"];
    channelLabel.backgroundColor =[StyleSheet colorFromHexString:@"#ffffff"];
    for(ChannelRank* c in self.channels){
        if([c.channel isEqualToString:channelLabel.text]){
            channel = c;
            break;
        }
    }
 
    [self updateChannel:channel];
}

-(void) updateChannel:(ChannelRank*) channel{
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
    for(int i=0;i<channel.ranks.count;i++){
        CGFloat wOffset = 0;
        Rank* rank = [channel.ranks objectAtIndex:i];
        UILabel* label =[self createLabel:[NSString stringWithFormat:@"%i",i+1] frame:CGRectMake(0, _tableOffset+i*ROW_HEIGHT, SCREEN_WIDTH*3/32, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:labelFont backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        wOffset+=SCREEN_WIDTH*3/32+2;
        [self.tableViews addObject:label];
        [self.contentView addSubview:label];
        label =[self createLabel:rank.name frame:CGRectMake(wOffset, _tableOffset+i*ROW_HEIGHT, SCREEN_WIDTH*186/320, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:labelFont backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        wOffset+=SCREEN_WIDTH*186/320+2;
        [self.contentView addSubview:label];
        [self.tableViews addObject:label];
        
        label =[self createLabel:[NSString stringWithFormat:@"%0.2f", rank.dayAmount.intValue/10000.0] frame:CGRectMake(wOffset, _tableOffset+i*ROW_HEIGHT, SCREEN_WIDTH*49/320, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:labelFont backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        wOffset+=SCREEN_WIDTH*49/320+2;
        [self.contentView addSubview:label];
        [self.tableViews addObject:label];
        
        label =[self createLabel:[NSString stringWithFormat:@"%0.2f%@", rank.rate.doubleValue*100,@"%"] frame:CGRectMake(wOffset, _tableOffset+i*ROW_HEIGHT,SCREEN_WIDTH-wOffset, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:labelFont backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        [self.contentView addSubview:label];
        [self.tableViews addObject:label];
    }
    
    self.contentView.contentSize =
    CGSizeMake(SCREEN_WIDTH, _tableOffset+channel.ranks.count*ROW_HEIGHT);
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
    _storeRankModel.delegate = nil;
    TT_RELEASE_SAFELY(_storeRankModel);
    TT_RELEASE_SAFELY(_channelLables);
    TT_RELEASE_SAFELY(_channels);
    TT_RELEASE_SAFELY(_tableViews);
    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
