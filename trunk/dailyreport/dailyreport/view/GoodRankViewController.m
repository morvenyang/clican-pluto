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
    CGFloat ROW_HEIGHT = 34;
    NSLog(@"%f",SCREEN_WIDTH);
    if(SCREEN_WIDTH==320){
        ROW_HEIGHT =40;
    }else if(SCREEN_WIDTH==375){
        ROW_HEIGHT =42;
    }else{
        ROW_HEIGHT =44;

    }

    if(y>=_tableOffset&&y<=_tableOffset+ROW_HEIGHT*self.goods.count){
        int index = (y-_tableOffset)/ROW_HEIGHT;
        NSLog(@"%i row is seelcted",index);
        GoodRank* gr = [self.goods objectAtIndex:index];
        DrAppDelegate.user.goods = self.goods;
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
    
    
    CGFloat ROW_HEIGHT = 34;
    CGFloat ROW_CONTENT_HEIGHT=32;
    int labelFontSize =14;
    NSLog(@"%f",SCREEN_WIDTH);
    if(SCREEN_WIDTH==320){
        labelFontSize =16;
        ROW_HEIGHT =40;
        ROW_CONTENT_HEIGHT =38;
    }else if(SCREEN_WIDTH==375){
        labelFontSize =18;
        ROW_HEIGHT =42;
        ROW_CONTENT_HEIGHT =40;
    }else{
        labelFontSize =20;
        ROW_HEIGHT =44;
        ROW_CONTENT_HEIGHT =42;
    }
    CGFloat wOffset = 0;
    CGFloat hOffset = dailyView.frame.size.height+2;
    [self.contentView addSubview:dailyView];
    [self.contentView addSubview:[self createLabel:@"＃" frame:CGRectMake(0, hOffset, SCREEN_WIDTH*30/320, SCREEN_HEIGHT*30/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    wOffset+=SCREEN_WIDTH*30/320+2;
    [self.contentView addSubview:[self createLabel:@"图片" frame:CGRectMake(wOffset, hOffset,ROW_CONTENT_HEIGHT, SCREEN_HEIGHT*30/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    wOffset+=ROW_CONTENT_HEIGHT+2;
    [self.contentView addSubview:[self createLabel:@"品名" frame:CGRectMake(wOffset, hOffset, SCREEN_WIDTH*144/320, SCREEN_HEIGHT*30/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
     wOffset+=SCREEN_WIDTH*144/320+2;
    [self.contentView addSubview:[self createLabel:@"颜色" frame:CGRectMake(wOffset, hOffset, SCREEN_WIDTH*39/320, SCREEN_HEIGHT*30/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
    wOffset+=SCREEN_WIDTH*39/320+2;
    [self.contentView addSubview:[self createLabel:@"件数" frame:CGRectMake(wOffset, hOffset,SCREEN_WIDTH-wOffset, SCREEN_HEIGHT*30/480) textColor:@"#ffffff" font:labelFontSize backgroundColor:STORE_RANK_TABLE_HEAD_COLOR textAlignment:ALIGN_CENTER]];
   
    hOffset+=SCREEN_HEIGHT*30/480;
    
    _tableOffset = hOffset;
    for(int i=0;i<goods.count;i++){
        wOffset=0;
        GoodRank* rank = [goods objectAtIndex:i];
        UILabel* label =[self createLabel:[NSString stringWithFormat:@"%i",i+1] frame:CGRectMake(0, hOffset+i*ROW_HEIGHT, SCREEN_WIDTH*30/320, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:labelFontSize+2 backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        wOffset+=SCREEN_WIDTH*30/320+2;
        [self.contentView addSubview:label];
        
        TTImageView* imageView = [[[TTImageView alloc] initWithFrame:CGRectMake(wOffset, hOffset+i*ROW_HEIGHT, ROW_CONTENT_HEIGHT, ROW_CONTENT_HEIGHT)] autorelease];
        imageView.urlPath = rank.imageLinkMin;
        [self.contentView addSubview:imageView];
        wOffset+=ROW_CONTENT_HEIGHT+2;
        
        label =[self createLabel:rank.name frame:CGRectMake(wOffset, hOffset+i*ROW_HEIGHT, SCREEN_WIDTH*144/320, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:labelFontSize+2 backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        [self.contentView addSubview:label];
        wOffset+=SCREEN_WIDTH*144/320+2;
        
        label =[self createLabel:rank.colorName frame:CGRectMake(wOffset, hOffset+i*ROW_HEIGHT, SCREEN_WIDTH*39/320, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:labelFontSize+2 backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        [self.contentView addSubview:label];
        wOffset+=SCREEN_WIDTH*39/320+2;
        label=[self createDecimalLabel:[NSNumber numberWithInt:rank.count.intValue]  frame:CGRectMake(wOffset, hOffset+i*ROW_HEIGHT,SCREEN_WIDTH-wOffset, ROW_CONTENT_HEIGHT) textColor:@"#6a6a6a" font:labelFontSize+2 backgroundColor:@"#f3f3f3" textAlignment:ALIGN_CENTER];
        [self.contentView addSubview:label];
    }
    
    self.contentView.contentSize =
    CGSizeMake(SCREEN_WIDTH, hOffset+goods.count*ROW_HEIGHT);
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
