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
    UIView* dailyView = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 34)] autorelease];
    NSString* imageName = [NSString stringWithFormat:@"每日收入%@背景.png",self.brand];
    dailyView.backgroundColor =[UIColor colorWithPatternImage:[UIImage imageNamed:imageName]];
    
    UIImageView* retailImageView = [self createImageViewFromNamedImage:@"图标-零售收入.png" frame:CGRectMake(0, 0, 34, 34)];
    
    
    UILabel* retailLabel = [self createLabel:@"店铺排名" frame:CGRectMake(40, 0, 120, 34) textColor:@"#ffffff" font:12 backgroundColor:nil];
    
    
    UIButton* calendarButton = [UIButton buttonWithType:UIButtonTypeCustom];
    calendarButton.frame =CGRectMake(160, 0, 34, 34);
    [calendarButton setImage:[UIImage imageNamed:@"图标-日历.png"] forState:UIControlStateNormal];
    [calendarButton addTarget:self action:@selector(openCalendar:) forControlEvents:UIControlEventTouchUpInside];
    
    NSDateFormatter *dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:@"MM月dd日 EEEE"];
    
    UILabel* calendarLabel = [self createLabel:[dateFormatter stringFromDate:date] frame:CGRectMake(200, 0, 120, 34) textColor:@"#ffffff" font:12 backgroundColor:nil];
    
    [dailyView addSubview:retailImageView];
    [dailyView addSubview:retailLabel];
    //[dailyView addSubview:calendarButton];
    [dailyView addSubview:calendarLabel];
    
    if([channels count]!=0){
        CGFloat width = 320.0/[channels count];
        int index = 0;
        
        for(ChannelRank* channelRank in channels){
            UILabel* channelLabel = [self createLabel:channelRank.channel frame:CGRectMake(0+index*width, 34, width, 50) textColor:@"#636363" font:20 backgroundColor:@"#ffffff"];
            UITapGestureRecognizer* recognizer = [[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(clickChannelLabel:)] autorelease];
            channelLabel.userInteractionEnabled = YES;
            [channelLabel addGestureRecognizer:recognizer];
            channelLabel.textAlignment = [self getAlignment:ALIGN_CENTER];
            if(index!=0){
                channelLabel.textColor = [UIColor whiteColor];
                channelLabel.backgroundColor = [StyleSheet colorFromHexString:@"#bdbdbd"];
            }
            [self.contentView addSubview:channelLabel];
            [self.channelLables addObject:channelLabel];
            index++;
        }
    }
    [self.contentView addSubview:dailyView];
    [self.contentView addSubview:[self createLabel:@"排名" frame:CGRectMake(0, 90, 50, 40) textColor:@"#ffffff" font:12 backgroundColor:@"#8f8f8f" textAlignment:ALIGN_CENTER]];
    [self.contentView addSubview:[self createLabel:@"店铺名称" frame:CGRectMake(52, 90, 166, 40) textColor:@"#ffffff" font:12 backgroundColor:@"#8f8f8f" textAlignment:ALIGN_CENTER]];
    [self.contentView addSubview:[self createLabel:@"零售额" frame:CGRectMake(220, 90, 49, 40) textColor:@"#ffffff" font:12 backgroundColor:@"#8f8f8f" textAlignment:ALIGN_CENTER]];
    [self.contentView addSubview:[self createLabel:@"占比" frame:CGRectMake(271, 90,49, 40) textColor:@"#ffffff" font:12 backgroundColor:@"#8f8f8f" textAlignment:ALIGN_CENTER]];
    if(self.channels.count>0){
        [self updateChannel:[self.channels objectAtIndex:0]];
    }
    
}
-(void)clickChannelLabel:(UIGestureRecognizer*)gestureRecognizer{
    UILabel* channelLabel = (UILabel*)gestureRecognizer.view;
    ChannelRank* channel = nil;
    for(UILabel* l in self.channelLables){
        l.textColor = [UIColor whiteColor];
        l.backgroundColor = [StyleSheet colorFromHexString:@"#bdbdbd"];
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
    [self.tableViews removeAllObjects];
    for(int i=0;i<channel.ranks.count;i++){
        Rank* rank = [channel.ranks objectAtIndex:i];
        UILabel* label =[self createLabel:[NSString stringWithFormat:@"%i",i+1] frame:CGRectMake(0, 130+i*42, 50, 40) textColor:@"#6a6a6a" font:12 backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        [self.tableViews addObject:label];
        [self.contentView addSubview:label];
        label =[self createLabel:rank.name frame:CGRectMake(52, 130+i*42, 166, 40) textColor:@"#6a6a6a" font:14 backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        [self.contentView addSubview:label];
        [self.tableViews addObject:label];
        
        label =[self createLabel:[NSString stringWithFormat:@"%0.1f", rank.dayAmount.intValue/10000.0] frame:CGRectMake(220, 130+i*42, 49, 40) textColor:@"#6a6a6a" font:12 backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        [self.contentView addSubview:label];
        [self.tableViews addObject:label];
        
        label =[self createLabel:[NSString stringWithFormat:@"%0.2f%@", rank.rate.doubleValue*100,@"%"] frame:CGRectMake(271, 130+i*42,49, 40) textColor:@"#6a6a6a" font:12 backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        [self.contentView addSubview:label];
        [self.tableViews addObject:label];
    }
    
    self.contentView.contentSize =
    CGSizeMake(320, 130+channel.ranks.count*42);
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
