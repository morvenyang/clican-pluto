//
//  GoodRankViewController.m
//  dailyreport
//
//  Created by zhang wei on 14-7-30.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "GoodRankViewController.h"
#import "AppDelegate.h"
#import "StyleSheet.h"
#define ROW_HEIGHT 40
#define ROW_CONTENT_HEIGHT 38

@implementation GoodRankViewController

@synthesize goodRankModel = _goodRankModel;

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
        self.goodRankModel = [[[GoodRankModel alloc] initWithBrand:self.brand delegate:self] autorelease];
        self.index = 5;

    }
    return self;
}
-(void)selectRow:(UITapGestureRecognizer*)gestureRecognizer{
    CGPoint location = [gestureRecognizer locationInView:self.contentView];
    NSLog(@"location y:%f",location.y);
    CGFloat y = location.y;
    if(y>=76&&y<=76+ROW_HEIGHT*self.goods.count){
        int index = (y-76)/ROW_HEIGHT;
        NSLog(@"%i row is seelcted",index);
        GoodRank* gr = [self.goods objectAtIndex:index];
        DrAppDelegate.user.goods = self.goods;
        DrAppDelegate.user.goodDate = self.selectedDate;
        DrAppDelegate.user.goodIndex = index;
        NSLog(@"good %@ is selected",gr.name);
        NSString* url = [NSString stringWithFormat:@"peacebird://good/%@/%i", [self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding],index];
        TTOpenURL(url);
    }
}
- (void)loadView
{
    [super loadView];
    UITapGestureRecognizer* recognizer = [[[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(selectRow:)] autorelease];
    [self.contentView addGestureRecognizer:recognizer];
    [self.goodRankModel load:TTURLRequestCachePolicyNone more:NO];
    if(IS_IPHONE5){
        self.contentView.scrollEnabled=NO;
    }
}

-(void)changeDateAndReload{
    [super changeDateAndReload];
    [self.goodRankModel load:TTURLRequestCachePolicyNone more:NO];
}

- (void) brandDidFinishLoad:(NSMutableArray*) goods date:(NSDate*) date{
    NSLog(@"%@",@"加载Brand Good Rank数据成功");
    self.selectedDate = date;
    self.goods = goods;
    UIView* dailyView =  [self createDailyView:@"图标-零售收入" label:@"商品排名"];
    
    
    
    [self.contentView addSubview:dailyView];
    [self.contentView addSubview:[self createLabel:@"＃" frame:CGRectMake(0, 36, 30, 40) textColor:@"#ffffff" font:14 backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    [self.contentView addSubview:[self createLabel:@"图片" frame:CGRectMake(32, 36, 38, 40) textColor:@"#ffffff" font:14 backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    [self.contentView addSubview:[self createLabel:@"品名" frame:CGRectMake(72, 36, 186, 40) textColor:@"#ffffff" font:14 backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    [self.contentView addSubview:[self createLabel:@"件数" frame:CGRectMake(260, 36,60, 40) textColor:@"#ffffff" font:14 backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
   
    for(int i=0;i<goods.count;i++){
        GoodRank* rank = [goods objectAtIndex:i];
        UILabel* label =[self createLabel:[NSString stringWithFormat:@"%i",i+1] frame:CGRectMake(0, 76+i*ROW_HEIGHT, 30, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:16 backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];

        [self.contentView addSubview:label];
        
        TTImageView* imageView = [[[TTImageView alloc] initWithFrame:CGRectMake(32, 76+i*ROW_HEIGHT, ROW_CONTENT_HEIGHT, ROW_CONTENT_HEIGHT)] autorelease];
        imageView.urlPath = rank.imageLinkMin;
        [self.contentView addSubview:imageView];

        
        label =[self createLabel:rank.name frame:CGRectMake(72, 76+i*ROW_HEIGHT, 186, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:16 backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        [self.contentView addSubview:label];
        label=[self createDecimalLabel:[NSNumber numberWithInt:rank.count.intValue]  frame:CGRectMake(260, 76+i*ROW_HEIGHT,60, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:16 backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        [self.contentView addSubview:label];
    }
    
    self.contentView.contentSize =
    CGSizeMake(320, 76+goods.count*ROW_HEIGHT);
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view.
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
    _goodRankModel.delegate = nil;
    TT_RELEASE_SAFELY(_goodRankModel);

    [super dealloc];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



@end
