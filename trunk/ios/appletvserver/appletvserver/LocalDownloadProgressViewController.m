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
#import "Constants.h"
#import "AtvUtil.h"
@implementation LocalDownloadProgressViewController

@synthesize refreshTimer = _refreshTimer;
@synthesize playerViewController = _playerViewController;
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
    if(TTIsPad()){
        self.navigationController.navigationBar.frame = CGRectMake(0, 20, self.navigationController.navigationBar.frame.size.width, 44);
    }else{
        self.navigationController.navigationBar.frame = CGRectMake(0, 20, self.navigationController.navigationBar.frame.size.width, 44);
    }

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
        NSString* content;
        if([record.fileType isEqualToString:FILE_TYPE_MP4]){
            content = [NSString stringWithFormat:@"<strong>%0.2fMB/%0.2fMB</strong>\n%@",record.downloadFileSize*1.0/(1024*1024),record.fileSize*1.0/(1024*1024),[record.url stringByReplacingOccurrencesOfString:@"&" withString:@"&amp;"]];
        }else{
            content = [NSString stringWithFormat:@"<strong>%ld/%ld</strong>\n%@",record.downloadFileSize,record.fileSize,[record.url stringByReplacingOccurrencesOfString:@"&" withString:@"&amp;"]];
        }
    
        OfflineRecordTableItem* item = [OfflineRecordTableItem itemWithText:[TTStyledText textFromXHTML:content lineBreaks:YES URLs:YES] URL:nil];
        if(!record.downloading){
            NSLog(@"add download button");
            item.deleteButton = [TargetButton buttonWithStyle:@"toolbarRoundButton:" title:@"删除" target:record];
            [item.deleteButton addTarget:self action:@selector(deleteAction:) forControlEvents:UIControlEventTouchUpInside];
        }else{
            item.deleteButton = nil;
        }
        
        if(([[NSFileManager defaultManager] fileExistsAtPath:record.filePath]&&[record.fileType isEqualToString:FILE_TYPE_MP4])||([record.fileType isEqualToString:FILE_TYPE_M3U8]&&record.fileSize==record.downloadFileSize)){
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
    record.downloading = NO;
    NSFileManager* fileManager = [NSFileManager defaultManager];
    NSError* error;
    [fileManager removeItemAtPath:[record.filePath stringByAppendingString:@".tmp"] error:&error];
    if(error){
        NSLog(@"error occured to delete %@, error:%@",[record.filePath stringByAppendingString:@".tmp"],error.description);
        error = nil;
    }
    [fileManager removeItemAtPath:[record.filePath stringByAppendingString:@".data"] error:&error];
    if(error){
        NSLog(@"error occured to delete %@, error:%@",[record.filePath stringByAppendingString:@".data"],error.description);
        error = nil;
    }
    [fileManager removeItemAtPath:record.filePath error:&error];
    if(error){
        NSLog(@"error occured to delete %@, error:%@",record.filePath,error.description);
    }
    [AppDele.offlineRecordProcess deleteOffileRecord:record];
}

-(void)playAction:(id)sender{
    TargetButton* button = (TargetButton*)sender;
    OfflineRecord* record = (OfflineRecord*)button.target;
    NSLog(@"play local cache file:%@",record.filePath);
    
    NSLog(@"play:%@",record.filePath);
    if([record.fileType isEqualToString:FILE_TYPE_MP4]){
        self.playerViewController = [[[MPMoviePlayerViewController alloc] initWithContentURL:[NSURL fileURLWithPath:record.filePath]] autorelease];
    }else{
        self.playerViewController = [[[MPMoviePlayerViewController alloc] initWithContentURL:[NSURL URLWithString:[NSString stringWithFormat:@"http://%@:8080/appletv/noctl/proxy/play.m3u8?url=%@",AppDele.ipAddress,[AtvUtil encodeURL:[@"file://" stringByAppendingString:record.filePath]]]]] autorelease];
    }
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(moviePlayBackDidFinish:)
                                                 name:MPMoviePlayerPlaybackDidFinishNotification object:self.playerViewController.moviePlayer];
    
    self.playerViewController.moviePlayer.controlStyle = MPMovieControlStyleFullscreen;
    
    [self presentMoviePlayerViewControllerAnimated:self.playerViewController];
    [self.playerViewController.moviePlayer prepareToPlay];
    [self.playerViewController.moviePlayer setFullscreen:YES animated:YES];
    [self.playerViewController.moviePlayer play];
}



- (void)moviePlayBackDidFinish:(NSNotification*)notification {
    
    MPMoviePlayerController *moviePlayer = [notification object];
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:MPMoviePlayerPlaybackDidFinishNotification
                                                  object:moviePlayer];
    
    if ([moviePlayer respondsToSelector:@selector(setFullscreen:animated:)]) {
        [moviePlayer.view removeFromSuperview];
    }
    
    [self.navigationController popViewControllerAnimated:NO];
}

-(void)resumeAction:(id)sender{
    TargetButton* button = (TargetButton*)sender;
    OfflineRecord* record = (OfflineRecord*)button.target;
    record.downloading = YES;
    if([record.fileType isEqualToString:FILE_TYPE_MP4]){
        record.downloadFileSize = 0;
    }
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
    TT_RELEASE_SAFELY(_playerViewController);
    [super dealloc];
}



-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    if([UIApplication sharedApplication].statusBarOrientation != UIInterfaceOrientationPortrait){
        [self performSelector:@selector(changeStatusBarOrientation) withObject:nil afterDelay:0.01];
    }
}
-(void) changeStatusBarOrientation{
    [UIApplication sharedApplication].statusBarOrientation = UIInterfaceOrientationPortrait;
}
@end
