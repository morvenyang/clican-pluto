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
    
    ConfigDataSource* ds = [[[ConfigDataSource alloc] initWithItems:items callback:self] autorelease];
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
