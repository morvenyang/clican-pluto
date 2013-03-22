//
//  QQChannelViewController.m
//  appletvserver
//
//  Created by zhang wei on 13-3-22.
//
//

#import "QQChannelViewController.h"

@implementation QQChannelViewController

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)createModel {
    NSMutableArray* items = [NSMutableArray array];
    TTTableTextItem* item1 = [TTTableTextItem itemWithText:@"电影" URL:@"atvserver://qq/index/14"];
    TTTableTextItem* item2 = [TTTableTextItem itemWithText:@"电视剧" URL:@"atvserver://qq/index/15"];
    [items addObject:item1];
    [items addObject:item2];
    TTListDataSource* ds = [[TTListDataSource alloc] initWithItems:items];
    self.dataSource = ds;
}

- (void)loadView
{
    [super loadView];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

@end
