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
#import "ConfigDataSource.h"

@implementation LocalDownloadProgressViewController

@synthesize refreshTimer = _refreshTimer;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"本地缓存";
        self.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"本地缓存" image:nil tag:3] autorelease];
    }
    return self;
}


#pragma mark -
#pragma mark TTModelViewController
- (void)createModel {
    NSMutableArray* items = [NSMutableArray array];
    
    NSArray* array = [AppDele.dbProcess getAllOfflineRecord];
    for(OfflineRecord* record in array){
        TTTableStyledTextItem* item = [TTTableStyledTextItem itemWithText:[TTStyledText textFromXHTML:[NSString stringWithFormat:@"%ldMB/%ldMB %@",record.downloadFileSize/(1024*1024),record.fileSize/(1024*1024),record.url] lineBreaks:YES URLs:NO] URL:nil];
        [items addObject:item];
    }
    
    TTListDataSource* ds = [[[TTListDataSource alloc] initWithItems:items] autorelease];
    self.dataSource = ds;
}

-(void)loadView {
    [super loadView];
    [self refreshView];
}

-(void)viewWillAppear:(BOOL)animated{
    if(self.refreshTimer==nil||![self.refreshTimer isValid]){
        NSLog(@"start refresh timer for download progress");
        self.refreshTimer = [NSTimer timerWithTimeInterval:3.0f target:self selector:@selector(refreshView) userInfo:nil repeats:YES];
        [[NSRunLoop mainRunLoop] addTimer:self.refreshTimer forMode:NSRunLoopCommonModes];
    }
}

-(void)viewDidDisappear:(BOOL)animated{
    NSLog(@"end refresh timer for download progress");
    [self.refreshTimer invalidate];
}
-(void)refreshView{
    _flags.isModelDidLoadInvalid = YES;
    [self invalidateView];
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
