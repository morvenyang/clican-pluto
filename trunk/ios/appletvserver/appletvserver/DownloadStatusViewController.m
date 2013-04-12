//
//  DownloadStatusViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-3-20.
//
//

#import "DownloadStatusViewController.h"
#import "DownloadStatusDataSource.h"
#import "AppDelegate.h"
#import "ffmpeg.h"
@implementation DownloadStatusViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = @"代理下载";
        self.tabBarItem = [[[UITabBarItem alloc] initWithTitle:@"代理下载" image:nil tag:2] autorelease];
    }
    return self;
}

- (id<UITableViewDelegate>)createDelegate {
    return [[[TTTableViewDragRefreshDelegate alloc] initWithController:self] autorelease];
}

- (void)createModel {
    DownloadStatusDataSource* ds = [[[DownloadStatusDataSource alloc] init] autorelease];
    self.dataSource = ds;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	TTButton* cancelButton = [TTButton buttonWithStyle:@"toolbarRoundButton:" title:@"取消当前代理下载"];
    
    [cancelButton setFont:[UIFont systemFontOfSize:14]];
    
    [cancelButton addTarget:self action:@selector(cancelAction) forControlEvents:UIControlEventTouchUpInside];
    [cancelButton sizeToFit];
    
    UIBarButtonItem* refreshItem = [[UIBarButtonItem alloc] initWithCustomView:cancelButton];
    
    [self.navigationItem setRightBarButtonItem:refreshItem animated:YES];
}

- (void)cancelAction{
    AppDele.m3u8Process.running =NO;
    AppDele.mp4Process.running =NO;
    transfer_code_interrupt = 1;
    AppDele.m3u8Process.m3u8Url=nil;
    AppDele.mp4Process.mp4Url=nil;
    AppDele.mkvProcess.mkvUrl = nil;
    [[AppDele queue] cancelAllOperations];
    [[AppDele queue] waitUntilAllOperationsAreFinished];
    [[AppDele queue] go];
    DownloadStatusDataSource* ds = [[[DownloadStatusDataSource alloc] init] autorelease];
    self.dataSource = ds;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
