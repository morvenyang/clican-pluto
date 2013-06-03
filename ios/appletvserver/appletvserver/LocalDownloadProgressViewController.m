//
//  LocalDownloadProgressViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-6-3.
//
//

#import "LocalDownloadProgressViewController.h"
#import "AppDelegate.h"
#import "OfflineRecord.h"

@implementation LocalDownloadProgressViewController

@synthesize refreshTimer = _refreshTimer;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"本地缓存";
        self.autoresizesForKeyboard = YES;
        self.variableHeightRows = YES;
    }
    return self;
}


#pragma mark -
#pragma mark TTModelViewController
- (void)createModel {
    [self refreshView];
}

-(void)loadView {
    [super loadView];
    [self refreshView];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    if(self.refreshTimer==nil||![self.refreshTimer isValid]){
        NSLog(@"start refresh timer for download progress");
        self.refreshTimer = [NSTimer timerWithTimeInterval:3.0f target:self selector:@selector(refreshView) userInfo:nil repeats:YES];
        [[NSRunLoop mainRunLoop] addTimer:self.refreshTimer forMode:NSRunLoopCommonModes];
    }
}

-(void)viewDidDisappear:(BOOL)animated{
    [super viewDidDisappear:animated];
    NSLog(@"end refresh timer for download progress");
    [self.refreshTimer invalidate];
}
-(void)refreshView{
    NSMutableArray* items = [NSMutableArray array];
    
    NSArray* array = [AppDele.offlineRecordProcess getAllOfflineRecord];
    for(OfflineRecord* record in array){
        NSString* content = [NSString stringWithFormat:@"<strong>%ldMB/%ldMB</strong>\n%@",record.downloadFileSize/(1024*1024),record.fileSize/(1024*1024),[record.url stringByReplacingOccurrencesOfString:@"&" withString:@"&amp;"]];
        NSLog(@"%@",record.filePath);
        NSLog(@"%@",record.url);
        TTTableStyledTextItem* item = [TTTableStyledTextItem itemWithText:[TTStyledText textFromXHTML:content lineBreaks:YES URLs:NO] URL:nil];
        
        [items addObject:item];
    }
    
    TTListDataSource* ds = [[[TTListDataSource alloc] initWithItems:items] autorelease];
    self.dataSource = ds;
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

-(void)dealloc{
    TT_RELEASE_SAFELY(_refreshTimer);
    [super dealloc];
}
@end
