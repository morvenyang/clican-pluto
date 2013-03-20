//
//  DownloadStatusViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-3-20.
//
//

#import "DownloadStatusViewController.h"
#import "DownloadStatusDataSource.h"


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
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
