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
#import "OfflineRecordTableItem.h"
#import "OfflineRecordDataSource.h"
#import "TargetButton.h"
@implementation LocalDownloadProgressViewController

@synthesize refreshTimer = _refreshTimer;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"离线缓存";
        self.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"离线缓存" image:nil tag:2] autorelease];
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
        NSString* content = [NSString stringWithFormat:@"<strong>%0.2fMB/%0.2fMB</strong>\n%@",record.downloadFileSize*1.0/(1024*1024),record.fileSize*1.0/(1024*1024),[record.url stringByReplacingOccurrencesOfString:@"&" withString:@"&amp;"]];
        NSLog(@"%@",record.filePath);
        NSLog(@"%@",record.url);
        OfflineRecordTableItem* item = [OfflineRecordTableItem itemWithText:[TTStyledText textFromXHTML:content lineBreaks:YES URLs:YES] URL:nil];
        item.deleteButton = [TargetButton buttonWithStyle:@"toolbarRoundButton:" title:@"删除" target:record];
        [item.deleteButton addTarget:self action:@selector(deleteAction:) forControlEvents:UIControlEventTouchUpInside];
        
        if([[NSFileManager defaultManager] fileExistsAtPath:record.filePath]){
            item.actionButton = [TargetButton buttonWithStyle:@"toolbarRoundButton:" title:@"播放" target:record];
            [item.actionButton addTarget:self action:@selector(playAction:) forControlEvents:UIControlEventTouchUpInside];
        }else{
            if(record.downloading){
                item.actionButton = [TargetButton buttonWithStyle:@"toolbarRoundButton:" title:@"暂停" target:record];
                [item.actionButton addTarget:self action:@selector(pauseAction:) forControlEvents:UIControlEventTouchUpInside];
            }else{
                item.actionButton = [TargetButton buttonWithStyle:@"toolbarRoundButton:" title:@"继续" target:record];
                [item.actionButton addTarget:self action:@selector(resumeAction:) forControlEvents:UIControlEventTouchUpInside];
            }
        }
        [items addObject:item];
    }
    OfflineRecordDataSource* ds = [[[OfflineRecordDataSource alloc] initWithItems:items] autorelease];
    self.dataSource = ds;
}

-(void)deleteAction:(id)sender{
    TargetButton* button = (TargetButton*)sender;
    OfflineRecord* record = (OfflineRecord*)button.target;
    [record.request clearDelegatesAndCancel];
    NSFileManager* fileManager = [NSFileManager defaultManager];
    [fileManager removeItemAtPath:[record.filePath stringByAppendingString:@".tmp"] error:nil];
    [fileManager removeItemAtPath:record.filePath error:nil];
    [AppDele.offlineRecordProcess deleteOffileRecord:record];
}

-(void)playAction:(id)sender{
    TargetButton* button = (TargetButton*)sender;
    OfflineRecord* record = (OfflineRecord*)button.target;
    NSLog(@"play local cache file:%@",record.filePath);
}

-(void)resumeAction:(id)sender{
    TargetButton* button = (TargetButton*)sender;
    OfflineRecord* record = (OfflineRecord*)button.target;
    record.downloading = YES;
    record.downloadFileSize = 0;
    [AppDele.downloadProcess downloadOfflineRecord:record];
    NSLog(@"resume downloading");
}

-(void)pauseAction:(id)sender{
    TargetButton* button = (TargetButton*)sender;
    OfflineRecord* record = (OfflineRecord*)button.target;
    [record.request clearDelegatesAndCancel];
    record.downloading = NO;
    NSLog(@"pause downloading");
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
